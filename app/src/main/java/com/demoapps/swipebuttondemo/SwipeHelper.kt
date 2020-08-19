package com.demoapps.swipebuttondemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.demoapps.swipebuttondemo.Models.Buttons
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


abstract class SwipeHelper(
    val context: Context,
    val recyclerView: RecyclerView,
    val buttonWidth: Int
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    var buttonList: ArrayList<Buttons>? = null
    lateinit var gestureDetector: GestureDetector
    var swipePos = -1
    var swipeThreshold = 0.5f
    var buttonBuffer: HashMap<Int, ArrayList<Buttons>>
    private var recoverQueue: LinkedList<Int>? = null

    abstract fun instatialteMyButton(
        viewHolder: RecyclerView.ViewHolder,
        buffer: ArrayList<Buttons>
    )

    private val gestureListener: SimpleOnGestureListener = object : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            for (button in buttonList!!) {
                if (button.onclick(e.x, e.y)) break
            }
            return true
        }
    }
    val onTouchListener = View.OnTouchListener { v, event ->
        if (swipePos < 0) {
            return@OnTouchListener false
        }
        val point = Point(event!!.getRawX().toInt(), event.getRawY().toInt())

        val swipedViewHolder =
            recyclerView.findViewHolderForAdapterPosition(swipePos)
        val rect = Rect()
        if(swipedViewHolder!= null){
            val swipedItem = swipedViewHolder.itemView
            swipedItem.getGlobalVisibleRect(rect)
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (rect.top < point.y && rect.bottom > point.y) gestureDetector.onTouchEvent(event) else {
                recoverQueue!!.add(swipePos)
                swipePos = -1
                recoverSwipedItem()
            }
        }
        return@OnTouchListener false
    }


    fun recoverSwipedItem() {
        while (!recoverQueue!!.isEmpty()) {
            val pos = recoverQueue!!.poll()!!.toInt()
            if (pos > -1) {
                recyclerView.adapter!!.notifyItemChanged(pos)
            }
        }
    }

    init {
        this.buttonList = ArrayList()
        this.gestureDetector = GestureDetector(context, gestureListener)
        this.recyclerView.setOnTouchListener(onTouchListener)
        buttonBuffer = HashMap()
        recoverQueue = IntLInkedSet()
        getSwipe()
    }

    fun getSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    class IntLInkedSet : LinkedList<Int>() {
        override fun contains(element: Int): Boolean {
            return false
        }

        override fun lastIndexOf(element: Int): Int {
            return element
        }

        override fun remove(element: Int): Boolean {
            return false
        }

        override fun indexOf(element: Int): Int {
            return element
        }

        override fun add(element: Int): Boolean {
            if (contains(element)) {
                return false
            } else {
                return super.add(element)
            }
        }

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val posi = viewHolder.adapterPosition
        if (swipePos != posi) {
            recoverQueue!!.add(swipePos)
        }
        swipePos = posi
        if (buttonBuffer.containsKey(swipePos)) {
            buttonList = buttonBuffer[swipePos]
        } else {
            buttonList!!.clear()
        }

        buttonBuffer.clear()
        swipeThreshold = 0.5f * buttonList!!.size.toFloat() * buttonWidth.toFloat()
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var transitionX = dX
        var itemView = viewHolder.itemView
        if (pos < 0) {
            swipePos = pos
            return
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (transitionX < 0) {
                var buffer: ArrayList<Buttons> = ArrayList()
                if (!buttonBuffer.containsKey(pos)) {
                    instatialteMyButton(viewHolder, buffer)
                    buttonBuffer.put(pos, buffer)
                } else {
                    buffer = buttonBuffer.get(pos)!!
                }
                transitionX = dX * buffer.size.toFloat() * buttonWidth.toFloat() / itemView.width
                drawButton(c, itemView, buffer, pos, transitionX)
            }

        }
        super.onChildDraw(c, recyclerView, viewHolder, transitionX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButton(c: Canvas, itemView: View, buffer: ArrayList<Buttons>, pos: Int, transitionX: Float) {
        var right = itemView.right.toFloat()
        var width = -1*transitionX / buffer.size
        for (btn in buffer){
            val left = right - width
            btn.onDraw(c, RectF(left,itemView.top.toFloat() , right, itemView.bottom.toFloat()),pos)
            right = left
        }
    }
}


