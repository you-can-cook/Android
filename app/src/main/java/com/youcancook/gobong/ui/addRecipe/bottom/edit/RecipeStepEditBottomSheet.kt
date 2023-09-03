package com.youcancook.gobong.ui.addRecipe.bottom.edit

import android.content.DialogInterface
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
import com.youcancook.gobong.model.RecipeStepAdded
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecipeStepEditBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetFrameBinding
    private val recipeAddBottomViewModel: RecipeStepEditBottomViewModel by activityViewModels()
    private var onSuccessListener: (Recipe) -> Unit? = { }
    private var onDeleteListener: (Long) -> Unit? = { }
    private var oldRecipe: RecipeStepAdded? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BottomSheetFrameBinding.inflate(inflater, container, false)
        childFragmentManager.beginTransaction().replace(
            R.id.bottomSheetFragment,
            RecipeStepEditBottomFragment()
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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipeAddBottomViewModel.isDeleted.collectLatest {
                    if (it) {
                        onDeleteDismiss()
                    }
                }
            }
        }

        oldRecipe?.let {
            recipeAddBottomViewModel.setOldRecipe(it)
        }

    }

    fun setOldRecipe(recipe: RecipeStepAdded) {
        oldRecipe = recipe
    }

    fun setOnSuccessListener(listener: (Recipe) -> Unit) {
        onSuccessListener = listener
    }

    fun setOnDeleteListener(listener: (Long) -> Unit) {
        onDeleteListener = listener
    }

    private fun onDeleteDismiss() {
        onDeleteListener(recipeAddBottomViewModel.getStepId())
        dismiss()
    }

    private fun onSuccessDismiss() {
        onSuccessListener(recipeAddBottomViewModel.getReplacedRecipe())
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        recipeAddBottomViewModel.resetSave()
    }

    companion object {
        const val TAG = "RecipeStepEditBottomFragment"
    }
}