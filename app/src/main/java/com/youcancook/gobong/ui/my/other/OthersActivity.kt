package com.youcancook.gobong.ui.my.other

import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.youcancook.gobong.R
import com.youcancook.gobong.adapter.GridItemDecorator
import com.youcancook.gobong.adapter.GridRecyclerViewListAdapter
import com.youcancook.gobong.databinding.ActivityOthersBinding
import com.youcancook.gobong.ui.base.NetworkActivity
import com.youcancook.gobong.ui.base.NetworkStateListener
import com.youcancook.gobong.ui.my.OthersProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OthersActivity :
    NetworkActivity<ActivityOthersBinding, OthersProfileViewModel>(R.layout.activity_others) {
    override val viewModel: OthersProfileViewModel by lazy {
        OthersProfileViewModel(appContainer.goBongRepository, appContainer.userRepository)
    }
    private val gridAdapter = GridRecyclerViewListAdapter(3, onFollowClick = {
        if (it.followed) {
            viewModel.unfollow()
        } else {
            viewModel.follow()
        }
    })

    private val gridItemDecorator =
        GridItemDecorator()

    override val onNetworkStateChange: NetworkStateListener = object : NetworkStateListener {
        override fun onSuccess() {
        }

        override fun onFail() {
        }

        override fun onDone() {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel

        intent?.getIntExtra(USER_ID, 0)?.let {
            println("userId $it")
            viewModel.setUserId(it)
            viewModel.getOthersInfo()
        }

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.user.collectLatest {
//                    println("user !!!!! $it")
//                    binding.followingButton.isSelected = it.followed
//                    if (it.followed) {
//                        (binding.followingButton).text = "팔로우"
//                    } else {
//                        (binding.followingButton).text = "팔로우"
//                    }
//                }
//            }
//        }

        binding.run {
            toolbar.setNavigationOnClickListener {
                if (it.isSelected) {
                    setGridRecyclerView()
                } else {
                    setLinearRecyclerView()
                }
                it.isSelected = it.isSelected.not()
            }

            setGridRecyclerView()

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

        }
    }

    private fun setGridRecyclerView() {
        binding.run {
            recyclerView.layoutManager = GridLayoutManager(this@OthersActivity, 3)
            gridAdapter.spanCount = 3
            recyclerView.removeItemDecoration(gridItemDecorator)
            recyclerView.addItemDecoration(gridItemDecorator.also { decorator ->
                decorator.margin =
                    resources.getDimension(R.dimen.grid_margin)
                        .toInt()
            })
            recyclerView.adapter = gridAdapter
        }
    }

    private fun setLinearRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
        gridAdapter.spanCount = 1
        gridAdapter.notifyItemRangeChanged(0, gridAdapter.itemCount)
    }

    companion object {
        val USER_ID = "userId"
    }
}