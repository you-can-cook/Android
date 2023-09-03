package com.youcancook.gobong.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.youcancook.gobong.R
import com.youcancook.gobong.adapter.RecipeListAdapter
import com.youcancook.gobong.databinding.ActivityDetailBinding
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.ui.base.NetworkActivity
import com.youcancook.gobong.ui.base.NetworkStateListener
import com.youcancook.gobong.ui.my.other.OthersActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class DetailActivity :
    NetworkActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail) {
    override val viewModel: DetailViewModel by lazy {
        DetailViewModel(appContainer.goBongRepository, appContainer.userRepository)
    }
    override val onNetworkStateChange: NetworkStateListener = object : NetworkStateListener {
        override fun onSuccess() {

        }

        override fun onFail() {
        }

        override fun onDone() {
        }

    }

    private val recipeAdapter = RecipeListAdapter(onItemClick = {
        viewModel.activeRecipeStep(it)
    })
    private val reviewDialog = ReviewDialogFragment(onDismissListener = {
        viewModel.setStar(it)
        viewModel.reviewRecipe()
    })

    private val deleteDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(this)
            .setTitle("레시피 삭제")
            .setMessage("레시피를 삭제하면 다시 복구할 수 없습니다.\n정말 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                viewModel.deleteRecipePost()
            }
            .setNegativeButton("취소") { _, _ ->
            }
            .setCancelable(true)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            vm = viewModel
            recyclerView.adapter = recipeAdapter
        }

        initListeners()

        intent?.getIntExtra(RECIPE_ID, 0)?.let {
            viewModel.getCurrentRecipe(it)
        }

        binding.run {

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.starCount.collectLatest {
                        if (it != 0) {
                            showReviewedStar(it)
                        }
                    }
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isMine.collectLatest {
                        if (it) setDisposable()
                    }
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isDeleted.collectLatest {
                        if (it) finish()
                    }
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.isBookmarked.collectLatest {
                        bookmarkImageView.isSelected = it
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.run {
            toolbar.setNavigationOnClickListener {
                finish()
            }

            toolTextView.setOnClickListener {
                MaterialAlertDialogBuilder(binding.root.context)
                    .setItems(viewModel.getTools().toTypedArray()) { _, _ -> }
                    .show()
            }

            bookmarkImageView.setOnClickListener {
                it.isSelected = it.isSelected.not()
                viewModel.bookmarkRecipe(it.isSelected)
            }

            profileImageView.setOnClickListener {
                val intent = Intent(this@DetailActivity, OthersActivity::class.java)
                    .putExtra(
                        OthersActivity.USER_ID,
                        viewModel.getId()
                    )
                binding.root.context.startActivity(intent)
            }

            followingButton.setOnClickListener {
                it.isSelected = it.isSelected.not()
                if (it.isSelected) {
                    viewModel.follow()
                    (it as Button).text = "팔로잉"
                } else {
                    viewModel.unfollow()
                    (it as Button).text = "팔로우"
                }
            }

            reviewButton.setOnClickListener {
                reviewDialog.setOldStar(viewModel.getStar())
                reviewDialog.show(supportFragmentManager, null)
            }

            editReviewButton.setOnClickListener {
                reviewDialog.setOldStar(viewModel.getStar())
                reviewDialog.show(supportFragmentManager, null)
            }

        }

    }

    private fun showReviewedStar(starCount: Int) {
        binding.run {
            val stars = listOf(star1, star2, star3, star4, star5)

            stars.map { it.isSelected = false }
            for (star in 0 until starCount) {
                stars[star].isSelected = true
            }
        }
    }

    private fun setDisposable() {
        binding.bookmarkImageView.run {
            setImageDrawable(
                AppCompatResources.getDrawable(
                    this@DetailActivity,
                    R.drawable.icon_delete
                )
            )
            setOnClickListener {
                deleteDialog.show()
            }
        }
    }

    companion object {
        val RECIPE_ID = "recipeId"
    }
}