package com.youcancook.gobong.ui.base

import android.util.Log
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class NetworkViewModel : GoBongViewModel() {

    private val _networkState = MutableStateFlow(NetworkState.DONE)
    val networkState: StateFlow<NetworkState> get() = _networkState

    protected fun setNetworkState(state: NetworkState) {
        _networkState.value = state
        if (state == NetworkState.LOADING) {
            CoroutineScope(Dispatchers.Unconfined).launch {
                delay(5000)
                finishNetwork()
            }
        }

        if (state == NetworkState.FAIL) {
            Log.e("GoBongBab", snackBarMessage.value)
        }
    }

    protected fun finishNetwork() {
        if (_networkState.value == NetworkState.LOADING) {
            _networkState.value = NetworkState.DONE
        }
    }

}