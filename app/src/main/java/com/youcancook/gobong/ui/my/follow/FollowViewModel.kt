package com.youcancook.gobong.ui.my.follow

import androidx.lifecycle.viewModelScope
import com.youcancook.gobong.model.User
import com.youcancook.gobong.model.UserProfile
import com.youcancook.gobong.model.repository.UserRepository
import com.youcancook.gobong.ui.base.NetworkViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FollowViewModel(
    private val userRepository: UserRepository,
) : NetworkViewModel() {

    private val _followers = MutableStateFlow<List<UserProfile>>(emptyList())
    val followers: StateFlow<List<UserProfile>> get() = _followers

    private val _followings = MutableStateFlow<List<UserProfile>>(emptyList())
    val followings: StateFlow<List<UserProfile>> get() = _followings

    fun getFollowerList() {
        viewModelScope.launch {
            try {
                requestFollowerList()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setSnackBarMessage(e.message ?: "")
                setNetworkState(NetworkState.FAIL)
            }
        }
    }

    private suspend fun requestFollowerList() {
        _followers.value = userRepository.getFollowerList()
    }

    fun getFollowingList() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestFollowingList()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setSnackBarMessage(e.message ?: "")
                setNetworkState(NetworkState.FAIL)
            }
        }
    }

    private suspend fun requestFollowingList() {
        _followings.value = userRepository.getFollowingList()
    }

    fun follow(userId: Int) {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestFollow(userId)
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    fun unfollow(userId: Int) {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestUnfollow(userId)
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestFollow(userId: Int) {
        userRepository.follow(userId)
    }

    private suspend fun requestUnfollow(userId: Int) {
        userRepository.unfollow(userId)
    }
}