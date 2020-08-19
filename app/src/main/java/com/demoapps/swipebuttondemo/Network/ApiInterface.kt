package com.demoapps.swipebuttondemo.Network

import com.google.gson.JsonElement
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @GET("3/movie/now_playing?api_key=1d9b898a212ea52e283351e521e17871&language=en")
    fun getInfo(): Observable<JsonElement>
}