package com.reece.linetvhomework.model

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    companion object {
        fun create(): ApiService {
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("http://www.mocky.io")
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("/v2/5a97c59c30000047005c1ed2")
    fun getDramas() : Deferred<Response<Model.Result>>
}