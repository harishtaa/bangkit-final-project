package com.akbar.dbdpv1.apinya

import com.akbar.dbdpv1.menu2.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("")
    fun getAllStories(
        //@Header("Authorization") authHeader: String
        @Path("id") id: String
    ): Call<UploadResponse> //masih belom ada call response

    @Multipart
    @POST("predict")
    fun uploadImage(
        //@Header("Authorization") authHeader: String,
        @Part file: MultipartBody.Part,
    ): Call<UploadResponse>
}