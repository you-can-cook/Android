package com.youcancook.gobong.ui.addRecipe.bottom.add

import androidx.lifecycle.viewModelScope
import com.youcancook.gobong.model.RecipeStepAdded
import com.youcancook.gobong.ui.addRecipe.bottom.RecipeStepBottomViewModel
import kotlinx.coroutines.launch

class RecipeStepAddBottomViewModel : RecipeStepBottomViewModel() {

    fun saveNewRecipeStep() {
        viewModelScope.launch {
            try {
                isSaveValidate()
                requestNewRecipe()
            } catch (e: Exception) {
                setToastMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestNewRecipe() {
        setSuccess(true)
    }

    fun getNewRecipeStep(): RecipeStepAdded {
        return RecipeStepAdded(
            "${minute.value}분 ${second.value}초",
            tools.value.filter { it.isChecked }.map { it.toolName },
            thumbnailByteArray.value,
            descriptionInputText.value
        )
    }
}