package com.youcancook.gobong.fake

import com.youcancook.gobong.model.LoginUser
import com.youcancook.gobong.model.RegisterUser
import com.youcancook.gobong.model.UserToken
import com.youcancook.gobong.model.repository.UserRepository

class FakeUserRepository : UserRepository {
    override suspend fun makeTemporaryToken(): String {
        throw Exception("네트워크 오류")
    }

    override suspend fun makeAccessToken(refreshToken: String): String {
        throw Exception("네트워크 오류")
    }

    override suspend fun login(user: LoginUser): UserToken {
        throw Exception("네트워크 오류")
    }

    override suspend fun register(registerUser: RegisterUser): UserToken {
        throw Exception("네트워크 오류")
    }

    override suspend fun follow(userId: String) {
        throw Exception("이미 팔로우한 사용자입니다.")
    }

    override suspend fun unfollow(userId: String) {
        throw Exception("팔로우하지 않은 사용자입니다.")
    }
}