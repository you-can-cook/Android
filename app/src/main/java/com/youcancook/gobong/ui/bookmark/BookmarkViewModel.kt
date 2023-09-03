package com.youcancook.gobong.ui.bookmark

import androidx.lifecycle.viewModelScope
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.repository.GoBongRepositoryImpl
import com.youcancook.gobong.model.repository.UserRepositoryImpl
import com.youcancook.gobong.ui.base.NetworkViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val goBongRepository: GoBongRepositoryImpl,
    private val userRepository: UserRepositoryImpl,
) : NetworkViewModel() {

    private val _recipes = MutableStateFlow<List<Card>>(emptyList())
    val recipes: StateFlow<List<Card>> = _recipes

    fun getBookmarkedRecipes() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestBookmarkedRecipes()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
            finishNetwork()
        }
    }

    private suspend fun requestBookmarkedRecipes() {
        _recipes.value = goBongRepository.getBookmarkedRecipes()
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