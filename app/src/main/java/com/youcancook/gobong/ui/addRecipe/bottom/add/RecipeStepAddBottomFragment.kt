package com.youcancook.gobong.ui.addRecipe.bottom.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.youcancook.gobong.ui.addRecipe.bottom.RecipeStepBottomFragment

class RecipeStepAddBottomFragment : RecipeStepBottomFragment() {
    override val viewModel: RecipeStepAddBottomViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            vm = viewModel
        }

        super.onViewCreated(view, savedInstanceState)

        binding.run {
            completeButton.setOnClickListener {
                viewModel.saveNewRecipeStep()
            }
        }

    }

}