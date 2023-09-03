package com.youcancook.gobong.ui.my.setting

import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.youcancook.gobong.R
import com.youcancook.gobong.model.repository.GoBongRepository
import com.youcancook.gobong.model.repository.UserRepository
import com.youcancook.gobong.model.repository.UserRepositoryImpl
import com.youcancook.gobong.ui.login.UserViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class SettingUserViewModel(
    private val goBongRepository: GoBongRepository,
    private val userRepository: UserRepository,
) : UserViewModel(userRepository) {

    fun getOldProfile() {
        viewModelScope.launch {
            try {
                requestOldProfile()
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestOldProfile() {
        val response = goBongRepository.getMyInfo()
        setProfileImage(response.profileUrl)
        nicknameInput.value = response.nickname

    }

    fun updateUserProfile() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestUpdateProfile()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestUpdateProfile() {
        if (isValidNickname().not()) {
            throw Exception("사용이 불가한 닉네임입니다")
        }

        userRepository.updateProfile(
            nicknameInput.value,
            profileImage.value,
            profileImageByteArray.value
        )
    }
}