package com.youcancook.gobong.ui.addRecipe.bottom.edit

import com.youcancook.gobong.model.RecipeStepAdded
import com.youcancook.gobong.ui.addRecipe.bottom.RecipeStepBottomViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipeStepEditBottomViewModel : RecipeStepBottomViewModel() {

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> get() = _isDeleted

    private var editId = 0L

    fun getStepId() = editId

    fun setOldRecipe(recipe: RecipeStepAdded) {
        setThumbnailByteArray(recipe.photoUrl)
        checkTools(recipe.tools)
        val times = recipe.time.split(" ")
        addMinute(times[0].removeSuffix("분").toInt())
        addSecond(times[1].removeSuffix("초").toInt())
        setDescriptionText(recipe.description)
        editId = recipe.id
    }

    fun updateRecipeStep() {
        try {
            isSaveValidate()
            requestUpdatedRecipeStep()
        } catch (e: Exception) {
            setSuccess(false)
            setSnackBarMessage(e.message ?: "")
        }
    }

    private fun requestUpdatedRecipeStep() {
        setSuccess(true)
    }

    fun removeRecipeStep() {
        _isDeleted.value = true
    }

    override fun resetSave() {
        super.resetSave()
        _isDeleted.value = false
    }

    fun getReplacedRecipe(): RecipeStepAdded {
        return RecipeStepAdded(
            "${minute.value}분 ${second.value}초",
            tools.value.filter { it.isChecked }.map { it.toolName },
            thumbnailByteArray.value,
            descriptionInputText.value,
            editId,
        )
    }
}