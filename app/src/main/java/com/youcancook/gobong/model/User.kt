package com.youcancook.gobong.model

import com.youcancook.gobong.model.network.dto.LoginDTO
import com.youcancook.gobong.model.network.dto.RegisterDTO
import java.io.Serializable

data class User(
    val profileUrl: String = "",
    val nickname: String = "쩝쩝박사",
    val userId: Int = 0,
    val recipe: String = "0",
    val follower: String = "0",
    val following: String = "0",
    val followed: Boolean = false,
) {
    companion object {
        fun createEmpty(): User {
            return User(
                "",
                "쩝쩝박사",
                0,
                "0",
                "0",
                "0",
                false
            )
        }
    }
}

data class UserProfile(
    val profileUrl: String = "",
    val nickname: String = "쩝쩝박사",
    val userId: Int = 0,
    val notMine: Boolean = true,
    val followed: Boolean = false,
)

data class LoginUser(
    val provider: String,
    val oAuthId: String,
    val temporaryToken: String,
) : Serializable

data class RegisterUser(
    val nickname: String,
    val provider: String,
    val oAuthId: String,
    val temporaryToken: String,
    val profileImageByteArray: ByteArray? = null,
)

data class UserToken(
    val accessToken: String,
    val refreshToken: String,
)

fun LoginUser.toLoginDTO(): LoginDTO {
    return LoginDTO(
        provider,
        oAuthId,
        temporaryToken
    )
}

fun RegisterUser.toRegisterDTO(): RegisterDTO {
    return RegisterDTO(
        nickname,
        provider,
        oAuthId,
        temporaryToken
    )
}
