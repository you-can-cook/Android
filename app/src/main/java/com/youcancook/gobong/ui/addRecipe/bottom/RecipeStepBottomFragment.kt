package com.youcancook.gobong.ui.addRecipe.bottom

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.BottomSheetRecipeStepDataBinding
import com.youcancook.gobong.ui.ImageActivity
import com.youcancook.gobong.ui.base.GoBongFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class RecipeStepBottomFragment :
    GoBongFragment<BottomSheetRecipeStepDataBinding>(R.layout.bottom_sheet_recipe_step_data) {
    private var imagePickActivityLauncher: ActivityResultLauncher<Intent>? = null
    abstract val viewModel: RecipeStepBottomViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        imagePickActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageUri = result.data?.getStringExtra(ImageActivity.IMAGE_DATA_TAG)
                    imageUri ?: return@registerForActivityResult
                    val imageData = ImageActivity.getImageByteArray(requireContext(), Uri.parse(imageUri))
                    imageData ?: return@registerForActivityResult
                    Log.e("GOBONG", "imageUri $imageUri $imageData")
                    viewModel.setThumbnailByteArray(imageData)
                }
            }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toastMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.run {

            photoImageView.setOnClickListener {
                getImageFromGallery()
            }

            toolsLayout.setOnCheckedStateChangeListener { group, checkedIds ->
                val views = checkedIds.map {
                    group.findViewById<Chip>(it).text.toString()
                }
                viewModel.checkTools(views)
            }

            showMoreToolsButton.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .add(R.id.bottomSheetFragment, RecipeStepSearchBottomFragment())
                    .addToBackStack(null)
                    .commit()
            }

            tenSecondChip.setOnClickListener {
                viewModel.addSecond(10)
            }

            thirtySecondChip.setOnClickListener {
                viewModel.addSecond(30)
            }

            oneMinuteChip.setOnClickListener {
                viewModel.addMinute(1)
            }

            fiveMinuteChip.setOnClickListener {
                viewModel.addMinute(5)
            }

            tenMinuteChip.setOnClickListener {
                viewModel.addMinute(10)
            }

            resetTimeImageView.setOnClickListener {
                viewModel.clearTime()
            }
        }
    }

    private fun getImageFromGallery() {
        Intent(requireActivity(), ImageActivity::class.java).run {
            imagePickActivityLauncher?.launch(this)
        }
    }

}