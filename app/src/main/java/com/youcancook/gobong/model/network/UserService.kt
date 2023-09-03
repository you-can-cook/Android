package com.youcancook.gobong.model.network

import com.youcancook.gobong.model.network.dto.FollowResponseDTO
import com.youcancook.gobong.model.network.dto.LoginDTO
import com.youcancook.gobong.model.network.dto.LoginResponseDTO
import com.youcancook.gobong.model.network.dto.RefreshTokenDTO
import com.youcancook.gobong.model.network.dto.RegisterDTO
import com.youcancook.gobong.model.network.dto.TemporaryTokenResponseDTO
import com.youcancook.gobong.model.network.dto.UpdateUserDTO
import com.youcancook.gobong.model.network.dto.UserInfoResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @POST("/api/users/temporary-token")
    suspend fun postTemporaryToken(

    ): Response<TemporaryTokenResponseDTO>

    @POST("/api/auth/reissue")
    suspend fun reissueAccessToken(
        @Body data: RefreshTokenDTO,
    ): Response<LoginResponseDTO>

    @POST("/api/users/login")
    suspend fun postLogin(
        @Body data: LoginDTO,
    ): Response<LoginResponseDTO>

    @POST("/api/users/signup")
    suspend fun postSignUp(
        @Body data: RegisterDTO,
    ): Response<LoginResponseDTO>

    @POST("/api/follow/{folloeeId}")
    suspend fun follow(
        @Header("Authorization") token: String,
        @Path("folloeeId") folloeeId: Int,
    ): Response<Void>

    @POST("/api/unfollow/{folloeeId}")
    suspend fun unfollow(
        @Header("Authorization") token: String,
        @Path("folloeeId") folloeeId: Int,
    ): Response<Void>

    @GET("/api/following")
    suspend fun getMyFollowing(
        @Header("Authorization") token: String,
    ): Response<List<FollowResponseDTO>>

    @GET("/api/unfollow/{folloeeId}")
    suspend fun getMyFollower(
        @Header("Authorization") token: String,
    ): Response<List<FollowResponseDTO>>

    @GET("/api/users")
    suspend fun getMyInfo(
        @Header("Authorization") token: String,
    ): Response<UserInfoResponseDTO>

    @GET("/api/users/{userId}")
    suspend fun getOthersInfo(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
    ): Response<UserInfoResponseDTO>

    @PATCH("/api/users")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body data: UpdateUserDTO,
    ): Response<Void>
}