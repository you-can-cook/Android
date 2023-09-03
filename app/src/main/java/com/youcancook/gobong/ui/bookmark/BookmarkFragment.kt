package com.youcancook.gobong.ui.bookmark

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import com.youcancook.gobong.R
import com.youcancook.gobong.adapter.GridItemDecorator
import com.youcancook.gobong.adapter.GridRecyclerViewListAdapter
import com.youcancook.gobong.databinding.FragmentBookmarkBinding
import com.youcancook.gobong.ui.base.NetworkFragment
import com.youcancook.gobong.ui.base.NetworkStateListener

class BookmarkFragment :
    NetworkFragment<FragmentBookmarkBinding, BookmarkViewModel>(R.layout.fragment_bookmark) {

    override val viewModel: BookmarkViewModel by lazy {
        BookmarkViewModel(appContainer.goBongRepository, appContainer.userRepository)
    }

    private val gridAdapter = GridRecyclerViewListAdapter(3, onFollowClick = {
        if (it.followed) {
            viewModel.unfollow(it.userId)
            (it as Button).text = "팔로우"
        } else {
            viewModel.follow(it.userId)
            (it as Button).text = "팔로잉"
        }
    })
    private val gridItemDecorator =
        GridItemDecorator()

    override val onNetworkStateChange: NetworkStateListener = object : NetworkStateListener {
        override fun onSuccess() {
            binding.swipeRefresh.isRefreshing = false
        }

        override fun onFail() {
            binding.swipeRefresh.isRefreshing = false
        }

        override fun onDone() {
            binding.swipeRefresh.isRefreshing = false
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            vm = viewModel
        }

        super.onViewCreated(view, savedInstanceState)

        binding.run {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            recyclerView.addItemDecoration(gridItemDecorator.also {
                it.margin =
                    requireContext().resources.getDimension(R.dimen.grid_margin).toInt()
            })
            recyclerView.adapter = gridAdapter
        }

        binding.run {

            toggleButton.setOnClickListener {
                if (it.isSelected) {
                    setGridRecyclerView()
                } else {
                    setLinearRecyclerView()
                }
                it.isSelected = it.isSelected.not()
            }

            swipeRefresh.setOnRefreshListener {
                viewModel.getBookmarkedRecipes()
            }
            setGridRecyclerView()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getBookmarkedRecipes()
    }

    private fun setGridRecyclerView() {
        binding.run {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            gridAdapter.spanCount = 3
            recyclerView.removeItemDecoration(gridItemDecorator)
            recyclerView.addItemDecoration(gridItemDecorator.also { decorator ->
                decorator.margin =
                    requireContext().resources.getDimension(R.dimen.grid_margin)
                        .toInt()
            })
            recyclerView.adapter = gridAdapter
        }
    }

    private fun setLinearRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        gridAdapter.spanCount = 1
        gridAdapter.notifyItemRangeChanged(0, gridAdapter.itemCount)
    }

}