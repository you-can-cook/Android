package com.youcancook.gobong.ui.my

import androidx.lifecycle.viewModelScope
import com.youcancook.gobong.model.repository.GoBongRepository
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.launch

class MyProfileViewModel(
    private val goBongRepository: GoBongRepository,
) : ProfileViewModel(goBongRepository) {

    fun getMyNickname() = user.value.nickname

    fun getMyInfo() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestMyInfo()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestMyInfo() {
        setUserInfo(goBongRepository.getMyInfo())
        setUserRecipe(goBongRepository.getMyRecipes())
    }
}