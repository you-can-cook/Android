package com.youcancook.gobong.ui.addRecipe.bottom

import com.youcancook.gobong.model.Tool
import com.youcancook.gobong.ui.base.GoBongViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class RecipeStepBottomViewModel : GoBongViewModel() {
    private val _isSavedSuccess = MutableStateFlow(false)
    val isSavedSuccess: StateFlow<Boolean> get() = _isSavedSuccess

    private val _thumbnailByteArray = MutableStateFlow(byteArrayOf())
    val thumbnailByteArray: StateFlow<ByteArray> get() = _thumbnailByteArray

    private val _tools = MutableStateFlow(
        listOf(
            Tool("전자레인지", false, true),
            Tool("에어프라이어", false, true),
            Tool("오븐", false, true),
            Tool("가스레인지", false, true),
            Tool("믹서", false, true),
            Tool("커피포트", false, true),
            Tool("프라이팬", false, true)
        )
    )
    val tools: StateFlow<List<Tool>> get() = _tools

    private val _minute = MutableStateFlow("0")
    val minute: StateFlow<String> get() = _minute

    private val _second = MutableStateFlow("0")
    val second: StateFlow<String> get() = _second

    val descriptionInputText = MutableStateFlow("")

    fun setSuccess(isSuccess: Boolean) {
        _isSavedSuccess.value = isSuccess
    }

    fun setThumbnailByteArray(byteArray: ByteArray) {
        _thumbnailByteArray.value = byteArray
    }

    fun setDescriptionText(text: String) {
        descriptionInputText.value = text
    }

    fun filterTools(searchInput: String) {
        _tools.value = if (searchInput.isEmpty()) {
            _tools.value.map {
                it.copy(isVisible = true)
            }
        } else {
            _tools.value.map {
                if (it.toolName.contains(searchInput)) it.copy(isVisible = true)
                else it.copy(isVisible = false)
            }

        }
    }

    fun checkTools(toolNames: List<String>) {
        _tools.value = _tools.value.map { tool ->
            if (tool.toolName in toolNames) tool.copy(isChecked = true)
            else if (tool.isVisible) {
                tool.copy(isChecked = false)
            } else {
                tool
            }
        }
    }

    fun clearTime() {
        _minute.value = "0"
        _second.value = "0"
    }

    fun addMinute(min: Int) {
        _minute.value = (_minute.value.toInt() + min).toString()
    }

    fun addSecond(sec: Int) {
        _second.value = (_second.value.toInt() + sec).toString()

        if (_second.value >= "60") {
            _second.value = (_second.value.toInt() - 60).toString()
            addMinute(1)
        }
    }

    protected fun isSaveValidate() {
        if (_thumbnailByteArray.value.isEmpty() && descriptionInputText.value.isEmpty())
            throw Exception("사진이나 설명을 입력해주세요")
    }

    open fun resetSave() {
        _thumbnailByteArray.value = ByteArray(0)
        _tools.value = _tools.value.map { it.copy(isChecked = false) }
        _minute.value = "0"
        _second.value = "0"
        descriptionInputText.value = ""

        _isSavedSuccess.value = false
    }
}