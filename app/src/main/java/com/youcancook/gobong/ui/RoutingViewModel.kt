package com.youcancook.gobong.ui

import androidx.lifecycle.viewModelScope
import com.youcancook.gobong.model.UserToken
import com.youcancook.gobong.model.repository.UserRepositoryImpl
import com.youcancook.gobong.ui.base.NetworkViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoutingViewModel(
    private val userRepository: UserRepositoryImpl,
) : NetworkViewModel() {

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> get() = _accessToken

    private var refreshToken = ""

    fun getToken() = UserToken(
        _accessToken.value,
        refreshToken
    )

    fun getAccessToken(refreshToken: String) {
        this.refreshToken = refreshToken
        setNetworkState(NetworkState.LOADING)
        viewModelScope.launch {
            try {
                getNewAccessToken(refreshToken)
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
            }
        }
    }

    private suspend fun getNewAccessToken(refreshToken: String) {
        _accessToken.value = userRepository.makeAccessToken(refreshToken)
    }

}