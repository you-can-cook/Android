package com.youcancook.gobong.ui.home

import androidx.lifecycle.viewModelScope
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.repository.GoBongRepository
import com.youcancook.gobong.model.repository.UserRepository
import com.youcancook.gobong.ui.base.NetworkViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val goBongRepository: GoBongRepository,
    private val userRepository: UserRepository,
) : NetworkViewModel() {

    private val _recipes = MutableStateFlow<List<Card>>(emptyList())
    val recipes: StateFlow<List<Card>> = _recipes

    fun getFollowingRecipes() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestFollowingRecipes()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
            finishNetwork()
        }
    }

    private suspend fun requestFollowingRecipes() {
        _recipes.value = goBongRepository.getFollowingRecipes()
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
        _recipes.value = _recipes.value.map {
            if (it.user.userId == userId) {
                it.copy(user = it.user.copy(followed = true))
            } else {
                it
            }
        }
    }

    private suspend fun requestUnfollow(userId: Int) {
        userRepository.unfollow(userId)
        _recipes.value = _recipes.value.map {
            if (it.user.userId == userId) {
                it.copy(user = it.user.copy(followed = false))
            } else {
                it
            }
        }
    }
}