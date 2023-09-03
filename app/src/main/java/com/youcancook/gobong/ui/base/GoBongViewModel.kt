package com.youcancook.gobong.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class GoBongViewModel : ViewModel() {

    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> get() = _toastMessage

    fun setSnackBarMessage(message: String) {
        _snackBarMessage.value = message
    }

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }
}