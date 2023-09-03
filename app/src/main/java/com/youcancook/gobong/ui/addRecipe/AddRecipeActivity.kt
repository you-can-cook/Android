package com.youcancook.gobong.ui.addRecipe

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.youcancook.gobong.R
import com.youcancook.gobong.adapter.RecipeListAdapter
import com.youcancook.gobong.adapter.bindingAdapter.addIngredient
import com.youcancook.gobong.databinding.ActivityAddRecipeBinding
import com.youcancook.gobong.model.RecipeStepAdded
import com.youcancook.gobong.ui.ImageActivity
import com.youcancook.gobong.ui.addRecipe.bottom.add.RecipeStepAddBottomSheet
import com.youcancook.gobong.ui.addRecipe.bottom.edit.RecipeStepEditBottomSheet
import com.youcancook.gobong.ui.base.NetworkActivity
import com.youcancook.gobong.ui.base.NetworkStateListener

class AddRecipeActivity :
    NetworkActivity<ActivityAddRecipeBinding, AddRecipeViewModel>(R.layout.activity_add_recipe) {
    override val viewModel: AddRecipeViewModel by lazy {
        AddRecipeViewModel(appContainer.goBongRepository)
    }
    override val onNetworkStateChange: NetworkStateListener = object : NetworkStateListener {
        override fun onSuccess() {
            finish()
        }

        override fun onFail() {
        }

        override fun onDone() {
        }

    }

    private var imagePickActivityLauncher: ActivityResultLauncher<Intent>? = null
    private val addStepBottomSheet = RecipeStepAddBottomSheet()
        .apply {
            setOnSuccessListener {
                //TODO 빈 값이면 리턴
                val data = it as RecipeStepAdded
                if (data.isEmpty()) return@setOnSuccessListener
                viewModel.addNewRecipeStep(data)
            }
        }

    private val editStepBottomSheet = RecipeStepEditBottomSheet()
        .apply {
            setOnSuccessListener {
                val data = it as RecipeStepAdded
                if (data.isEmpty()) return@setOnSuccessListener
                viewModel.replaceNewRecipeStep(data)
            }
            setOnDeleteListener {
                viewModel.deleteStep(it)
            }
        }

    private val recipeAdapter = RecipeListAdapter(onEditItemClick = {
        editStepBottomSheet.show(supportFragmentManager, RecipeStepEditBottomSheet.TAG)
        editStepBottomSheet.apply {
            setOldRecipe(it as RecipeStepAdded)
        }
    }, onAddItemClick = {
        addStepBottomSheet.show(supportFragmentManager, RecipeStepAddBottomSheet.TAG)
    })

    private val closeAlertDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(this)
            .setMessage("레시피 게시를 종료하시겠습니까?")
            .setPositiveButton("네") { _, _ ->
                finish()
            }
            .setNegativeButton("아니오") { _, _ ->

            }
            .setCancelable(true)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            vm = viewModel
        }

        imagePickActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {

                    val imageUri = result.data?.getStringExtra(ImageActivity.IMAGE_DATA_TAG)
                    imageUri ?: return@registerForActivityResult
                    val imageData = ImageActivity.getImageByteArray(this, Uri.parse(imageUri))
                    imageData ?: return@registerForActivityResult
                    Log.e("GOBONG", "imageUri $imageUri $imageData")
                    viewModel.setThumbnailByteArray(imageData)
                }
            }

        initListeners()

    }

    private fun initListeners() {
        binding.run {
            rootLayout.setOnClickListener {
                addIngredientEditText.clearFocus()
            }

            closeButton.setOnClickListener {
                closeAlertDialog.show()
            }

            uploadTextView.setOnClickListener {
                val ingredients = ingredientGroup.children.filter {
                    it.id != R.id.addIngredientEditText
                            && it.id != R.id.addIngredientButton
                }.map {
                    val view = it as Chip
                    view.text.toString()
                }.toList()
                viewModel.uploadNewRecipePost(ingredients)
            }

            thumbnailImageView.setOnClickListener {
                getImageFromGallery()
            }

            addIngredientButton.setOnClickListener {
                addIngredientEditText.clearFocus()
                addIngredientEditText.isVisible = true
            }

            addIngredientEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus.not()) {
                    val oldText = viewModel.getIngredientInputText()
                    if (oldText.isNotEmpty()) {
                        ingredientGroup.addIngredient(oldText)
                        addIngredientEditText.text.clear()
                    }
                    addIngredientEditText.isVisible = false
                }
            }

            levelGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                val view = findViewById<Button>(checkedId)
                viewModel.setLevel(view.text.toString())
            }

            recyclerView.adapter = recipeAdapter

        }
    }

    private fun getImageFromGallery() {
        Intent(this, ImageActivity::class.java).run {
            imagePickActivityLauncher?.launch(this)
        }

    }
}