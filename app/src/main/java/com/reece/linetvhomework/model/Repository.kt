package com.reece.linetvhomework.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {

    suspend fun getDramas() : Model.Result? {
        return withContext(Dispatchers.Main) {
            val apiService = ApiService.create()
            val result = apiService.getDramas().await().body()

            result
        }
    }
}