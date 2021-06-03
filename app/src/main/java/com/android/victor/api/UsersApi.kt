package com.android.victor.api

import com.android.victor.model.PredictResponse
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UsersApi {
    @Multipart
    @POST("predict")
    fun predict(
        @Part("data") data: JSONArray
    ): Call<PredictResponse>
}