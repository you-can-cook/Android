package com.youcancook.gobong.ui.addRecipe.bottom.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.BottomSheetFrameBinding
import com.youcancook.gobong.model.Recipe
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecipeStepAddBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetFrameBinding
    private val recipeAddBottomViewModel: RecipeStepAddBottomViewModel by activityViewModels()
    private var onSuccessListener: (Recipe) -> Unit? = { }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetFrameBinding.inflate(inflater, container, false)
        childFragmentManager.beginTransaction().replace(
            R.id.bottomSheetFragment,
            RecipeStepAddBottomFragment()
        ).commit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeAddBottomViewModel.isSavedSuccess.collectLatest {
                    if (it) {
                        onSuccessDismiss()
                    }
                }
            }
        }
    }

    fun setOnSuccessListener(listener: (Recipe) -> Unit) {
        onSuccessListener = listener
    }

    private fun onSuccessDismiss() {
        onSuccessListener(recipeAddBottomViewModel.getNewRecipeStep())
        recipeAddBottomViewModel.resetSave()
        dismiss()
    }

    companion object {
        const val TAG = "RecipeStepAddBottomFragment"
    }
}