package com.akbar.dbdpv1.apinya

import com.akbar.dbdpv1.menu2.*
import com.google.gson.JsonParser
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("predict")
    fun uploadImage(
        //@Header("Authorization") authHeader: String,
        @Body body:UploadRequest
    ): Call<UploadResponse>
}