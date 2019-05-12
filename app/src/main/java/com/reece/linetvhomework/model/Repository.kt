package com.reece.linetvhomework.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.ConnectivityManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object Repository {
    private const val LOCAL_CACHE = "localCache"
    private const val LOCAL_CACHE_DRAMAS = "localCacheDramas"

    suspend fun getDramas(context: Context): Pair<Model.Result?, Boolean> {
        return withContext(Dispatchers.Main) {

            if (isNetworkConnected(context)) {
                val apiService = ApiService.create()
                val result = apiService.getDramas().await().body()

                val json = Gson().toJson(result)
                handleLocalCache(context, json)

                return@withContext Pair(result, true)
            } else {
                return@withContext Pair(fetchFromLocalCache(context), false)
            }
        }
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    private fun handleLocalCache(context: Context, json: String) {
        val pref = context.getSharedPreferences(LOCAL_CACHE, MODE_PRIVATE)
        pref.edit()
            .putString(LOCAL_CACHE_DRAMAS, json)
            .apply()
    }

    private fun fetchFromLocalCache(context: Context): Model.Result? {
        val pref = context.getSharedPreferences(LOCAL_CACHE, MODE_PRIVATE)

        return if (pref.contains(LOCAL_CACHE_DRAMAS)) {
            val json = pref.getString(LOCAL_CACHE_DRAMAS, "")
            Gson().fromJson(json, Model.Result::class.java)
        } else {
            null
        }
    }
}