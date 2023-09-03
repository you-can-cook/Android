package com.youcancook.gobong.model.datasource

import android.util.Log
import com.youcancook.gobong.model.LoginUser
import com.youcancook.gobong.model.RegisterUser
import com.youcancook.gobong.model.UserProfile
import com.youcancook.gobong.model.UserToken
import com.youcancook.gobong.model.network.ImageService
import com.youcancook.gobong.model.network.UserService
import com.youcancook.gobong.model.network.dto.RefreshTokenDTO
import com.youcancook.gobong.model.network.dto.UpdateUserDTO
import com.youcancook.gobong.model.network.dto.toUserProfile
import com.youcancook.gobong.model.network.dto.toUserToken
import com.youcancook.gobong.model.toLoginDTO
import com.youcancook.gobong.model.toRegisterDTO
import com.youcancook.gobong.util.ACCESS_TOKEN
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserDataSource(
    private val userService: UserService,
    private val imageService: ImageService,
) {
    private fun getToken(): String {
        return "Bearer $ACCESS_TOKEN"
    }

    suspend fun requestTemporaryToken(): String {
        val response = userService.postTemporaryToken()
        if (response.isSuccessful) {
            return response.body()?.temporaryToken ?: throw Exception("네트워크 요청에 실패했습니다")
        } else {
            throw Exception("네트워크 요청에 실패했습니다")
        }
    }

    suspend fun requestAccessToken(refreshToken: String): String {
        val response = userService.reissueAccessToken(RefreshTokenDTO(refreshToken))
        Log.e(
            "Gobongbab", "$refreshToken ${response.body()} ${
                response.errorBody()?.string()
            }"
        )
        if (response.isSuccessful) {
            return response.body()?.toUserToken()?.accessToken ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun requestLogin(user: LoginUser): UserToken {
        val response = userService.postLogin(user.toLoginDTO())
        if (response.isSuccessful) {
            return response.body()?.toUserToken() ?: throw Exception("존재하지 않는 사용자입니다")
        } else {
            throw Exception("존재하지 않는 사용자입니다")
        }
    }

    suspend fun requestRegister(registerUser: RegisterUser): UserToken {
        var request = registerUser.toRegisterDTO()
        registerUser.profileImageByteArray?.let {
            val url = getImageUrlByByteArray(registerUser.profileImageByteArray)
            request = request.copy(profileImageURL = url)
        }

        val response = userService.postSignUp(request)
        if (response.isSuccessful) {
            return response.body()?.toUserToken() ?: throw Exception(response.message())
        } else {
            throw Exception("중복된 닉네임입니다")
        }
    }

    suspend fun requestFollow(userId: Int) {
        val response = userService.follow(getToken(), userId)
        Log.e(
            "GoBongBab",
            "follow $response ${response.errorBody()?.string()}"
        )
        if (response.isSuccessful.not()) {
            throw Exception("이미 팔로우한 사용자입니다.")
        }
    }

    suspend fun requestUnfollow(userId: Int) {
        val response = userService.unfollow(getToken(), userId)
        Log.e(
            "GoBongBab",
            "unfollow $response ${response.errorBody()?.string()}"
        )
        if (response.isSuccessful.not()) {
            throw Exception("팔로우하지 않은 사용자입니다.")
        }
    }

    suspend fun requestMyFollowerList(): List<UserProfile> {
        val response = userService.getMyFollower(getToken())
        Log.e(
            "GoBongBab",
            "myFollower $response ${response.errorBody()?.string()}"
        )
        if (response.isSuccessful) {
            return response.body()?.map { it.toUserProfile() } ?: throw Exception("")
        } else {
            throw Exception("")
        }
    }

    suspend fun requestMyFollowingList(): List<UserProfile> {
        val response = userService.getMyFollowing(getToken())
        Log.e(
            "GoBongBab",
            "myFollowing $response ${response.errorBody()?.string()}"
        )
        if (response.isSuccessful) {
            return response.body()?.map { it.toUserProfile() } ?: throw Exception("")
        } else {
            throw Exception("")
        }
    }

    suspend fun requestUpdateProfile(
        nickname: String,
        oldProfileImageUrl: String,
        newProfileByteArray: ByteArray,
    ) {
        val userDTO = if (newProfileByteArray.isEmpty()) {
            UpdateUserDTO(nickname, oldProfileImageUrl)

        } else {
            UpdateUserDTO(
                nickname,
                getImageUrlByByteArray(newProfileByteArray)
            )
        }
        userService.updateProfile(getToken(), userDTO)
    }

    private suspend fun getImageUrlByByteArray(imageByte: ByteArray): String {
        val requestFile = RequestBody.create(MediaType.parse("image/*"), imageByte);
        val uploadFile = MultipartBody.Part.createFormData("image", "", requestFile);
        val response = imageService.postImage(uploadFile)
        if (response.isSuccessful) {
            return response.body()?.imageUrl ?: throw Exception("이미지 업로드에 실패했습니다")
        } else {
            throw Exception("이미지 업로드에 실패했습니다")
        }
    }

}