package com.youcancook.gobong.model.repository

import com.youcancook.gobong.model.LoginUser
import com.youcancook.gobong.model.RegisterUser
import com.youcancook.gobong.model.UserProfile
import com.youcancook.gobong.model.UserToken
import com.youcancook.gobong.model.datasource.UserDataSource

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
) : UserRepository {

    override suspend fun makeTemporaryToken(): String {
        return userDataSource.requestTemporaryToken()
    }

    override suspend fun makeAccessToken(refreshToken: String): String {
        return userDataSource.requestAccessToken(refreshToken)
    }

    override suspend fun login(user: LoginUser): UserToken {
        return userDataSource.requestLogin(user)
    }

    override suspend fun register(registerUser: RegisterUser): UserToken {
        return userDataSource.requestRegister(registerUser)

    }

    override suspend fun follow(userId: Int) {
        userDataSource.requestFollow(userId)
    }

    override suspend fun unfollow(userId: Int) {
        userDataSource.requestUnfollow(userId)
    }

    override suspend fun getFollowerList(): List<UserProfile> {
        return userDataSource.requestMyFollowerList()
    }

    override suspend fun getFollowingList(): List<UserProfile> {
        return userDataSource.requestMyFollowingList()
    }

    override suspend fun updateProfile(
        nickname: String,
        oldProfileImageUrl: String,
        newProfileByteArray: ByteArray,
    ) {
        userDataSource.requestUpdateProfile(nickname, oldProfileImageUrl, newProfileByteArray)
    }

}