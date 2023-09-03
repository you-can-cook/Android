package com.youcancook.gobong.model.network

import com.youcancook.gobong.model.network.dto.ImageUrlResponseDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageService {

    @Multipart
    @POST("/api/images/upload")
    suspend fun postImage(
        @Part file: MultipartBody.Part,
    ): Response<ImageUrlResponseDTO>

}