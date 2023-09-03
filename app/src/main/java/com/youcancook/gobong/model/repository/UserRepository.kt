package com.youcancook.gobong.model.repository

import com.youcancook.gobong.model.LoginUser
import com.youcancook.gobong.model.RegisterUser
import com.youcancook.gobong.model.UserProfile
import com.youcancook.gobong.model.UserToken

interface UserRepository {

    suspend fun makeTemporaryToken(): String

    suspend fun makeAccessToken(refreshToken: String): String

    suspend fun login(user: LoginUser): UserToken

    suspend fun register(registerUser: RegisterUser): UserToken

    suspend fun follow(userId: Int)

    suspend fun unfollow(userId: Int)

    suspend fun getFollowerList(): List<UserProfile>

    suspend fun getFollowingList(): List<UserProfile>

    suspend fun updateProfile(
        nickname: String,
        oldProfileImageUrl: String,
        newProfileByteArray: ByteArray,
    )

}