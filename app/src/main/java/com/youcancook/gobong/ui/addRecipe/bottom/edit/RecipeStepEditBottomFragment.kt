package com.youcancook.gobong.ui.addRecipe.bottom.edit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.youcancook.gobong.ui.addRecipe.bottom.RecipeStepBottomFragment

class RecipeStepEditBottomFragment : RecipeStepBottomFragment() {
    override val viewModel: RecipeStepEditBottomViewModel by activityViewModels()

    private val deleteDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("단계 삭제")
            .setMessage("단계를 삭제하면 다시 복구할 수 없습니다.\n정말 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                viewModel.removeRecipeStep()
            }
            .setNegativeButton("취소") { _, _ ->

            }
            .setCancelable(true)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            vm = viewModel
        }
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            completeButton.setOnClickListener {
                viewModel.updateRecipeStep()
            }

            deleteButton.isVisible = true

            deleteButton.setOnClickListener {
                deleteDialog.show()
            }
        }
    }
}