package com.youcancook.gobong.model.network.dto

import com.google.gson.annotations.SerializedName
import com.youcancook.gobong.model.UserProfile

data class RegisterDTO(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("oauthId") val oAuthId: String,
    @SerializedName("temporaryToken") val temporaryToken: String,
    @SerializedName("profileImageURL") val profileImageURL: String? = null,
)

data class LoginDTO(
    @SerializedName("provider") val provider: String,
    @SerializedName("oauthId") val oAuthId: String,
    @SerializedName("temporaryToken") val temporaryToken: String,
)

data class RefreshTokenDTO(
    @SerializedName("refreshToken") val refreshToken: String,
)

data class UpdateUserDTO(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageURL") val profileImageURL: String,
)