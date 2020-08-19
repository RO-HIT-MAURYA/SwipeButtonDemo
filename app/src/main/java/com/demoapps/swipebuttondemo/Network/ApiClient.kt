package com.demoapps.swipebuttondemo.Network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null
        @Throws(Exception::class)
        fun getClient(): Retrofit? {
            if (retrofit == null) {
                val okHttpClient = OkHttpClient().newBuilder()
                    .readTimeout(900000, TimeUnit.MILLISECONDS)
                    .connectTimeout(900000, TimeUnit.MILLISECONDS)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()

                retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl("https://api.themoviedb.org/").client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build()

            }
            return retrofit
        }
    }
}
