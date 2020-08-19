package com.demoapps.swipebuttondemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demoapps.swipebuttondemo.Adapter.MovieListAdapter
import com.demoapps.swipebuttondemo.Interface.MyButtonClickListener
import com.demoapps.swipebuttondemo.Models.Buttons
import com.demoapps.swipebuttondemo.Models.ResponseModel
import com.demoapps.swipebuttondemo.Network.ApiClient
import com.demoapps.swipebuttondemo.Network.ApiInterface
import com.google.android.material.badge.BadgeUtils
import com.google.gson.Gson
import com.google.gson.JsonElement
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var observable: Observable<JsonElement>? = null
    var disposable: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSwipe()
        getData()
    }

    fun getSwipe(){
        val swipe = object : SwipeHelper(this, rv_list,200){
            override fun instatialteMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: ArrayList<Buttons>
            ) {
                buffer.add(Buttons(this@MainActivity,"Share",30,R.drawable.ic_share, Color.parseColor("#109A0B"),object : MyButtonClickListener{
                    override fun onClickButton(pos: Int) {

                    }
                }))

                buffer.add(Buttons(this@MainActivity,"Delete",30,R.drawable.ic_delete,Color.parseColor("#ED138B"),object : MyButtonClickListener{
                    override fun onClickButton(pos: Int) {

                    }
                }))


                buffer.add(Buttons(this@MainActivity,"Duplicate",30,R.drawable.ic_content_copy_black_24dp,Color.parseColor("#FFC107"),object : MyButtonClickListener{
                    override fun onClickButton(pos: Int) {

                    }
                }))
            }

        }

    }

    fun getData(){
        val apiInterface = ApiClient.getClient()?.create(ApiInterface::class.java)
        apiInterface.let {
            observable = it!!.getInfo()
        }

        if (observable != null) {
            disposable = observable!!.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        val response = Gson().fromJson(result, ResponseModel::class.java)
                        parseResponse(response)
                    },
                    { error ->
                        Toast.makeText(this ,"Some Error Occured", Toast.LENGTH_SHORT).show()
                    }
                )
        }
    }

    fun parseResponse(res : ResponseModel?){
        if(res!= null){
            rv_list.setHasFixedSize(true)
            rv_list.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = MovieListAdapter(this@MainActivity,res.results)
            }
        }
    }
}
