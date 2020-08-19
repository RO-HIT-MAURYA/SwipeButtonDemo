package com.demoapps.swipebuttondemo.Models

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.demoapps.swipebuttondemo.Interface.MyButtonClickListener


class Buttons(
    val context: Context,
    val text: String,
    val textSize: Int,
    val imageResId: Int,
    val color: Int,
    val listener: MyButtonClickListener
) {
    var pos = 0
    var clickRegion: RectF? = null
    val resourse: Resources

    init {
        resourse = context.resources
    }

    fun onclick(x: Float, y: Float): Boolean {
        if (clickRegion != null && clickRegion!!.contains(x, y)) {
            listener.onClickButton(pos)
            return true
        }

        return false
    }

    fun onDraw(canvas: Canvas, rect: RectF, pos: Int) {
        val paint = Paint()
        paint.color = color
        canvas.drawRect(rect, paint)

        paint.color = Color.WHITE
        paint.textSize = textSize.toFloat()

        val r = Rect()
        val cHeight = rect.height()
        val cWidth = rect.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(text, 0, text.length, r)
        var x = 0f
        var y = 0f
        val drawable = ContextCompat.getDrawable(context, imageResId)
        val bitmap = drawbletoBitmap(drawable!!)
        canvas.drawBitmap(bitmap, (rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2, paint)

        x =  cWidth / 2f - r.width() / 2f - r.left + bitmap.width / 2
        y =  cHeight / 2f + r.height() / 2f - r.bottom + bitmap.height * 1.5f

        canvas.drawText(text, rect.left + x, rect.top + y, paint)
        clickRegion = rect
        this.pos = pos

    }

    fun drawbletoBitmap(d: Drawable): Bitmap {
        if (d is BitmapDrawable) {
            return d.bitmap
        }
        val bitmap =
            Bitmap.createBitmap(d.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)
        return bitmap
    }
}