package com.youcancook.gobong.ui.my

import androidx.lifecycle.viewModelScope
import com.youcancook.gobong.model.User
import com.youcancook.gobong.model.repository.GoBongRepository
import com.youcancook.gobong.model.repository.UserRepository
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OthersProfileViewModel(
    private val goBongRepository: GoBongRepository,
    private val userRepository: UserRepository,
) : ProfileViewModel(goBongRepository) {
    private val _userId = MutableStateFlow(0)
    val userId: StateFlow<Int> get() = _userId

    fun setUserId(userId: Int) {
        _userId.value = userId
    }

    private fun setOthersInfo(user: User) {
        setUserInfo(user)
    }

    fun getOthersInfo() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestOthersInfo()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestOthersInfo() {
        setUserInfo(goBongRepository.getOthersInfo(userId.value))
        setUserRecipe(goBongRepository.getOthersRecipes(userId.value))
    }

    fun follow() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestFollow()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    fun unfollow() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestUnfollow()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestFollow() {
        userRepository.follow(getUserInfo().userId)
        setOthersInfo(getUserInfo().copy(followed = true))
    }

    private suspend fun requestUnfollow() {
        userRepository.unfollow(getUserInfo().userId)
        setOthersInfo(getUserInfo().copy(followed = false))
    }
}