package com.youcancook.gobong.ui.my.follow

import android.os.Bundle
import android.view.View
import com.youcancook.gobong.R
import com.youcancook.gobong.adapter.FollowListAdapter
import com.youcancook.gobong.databinding.FragmentFollowingBinding
import com.youcancook.gobong.ui.base.NetworkFragment
import com.youcancook.gobong.ui.base.NetworkStateListener

class FollowingFragment :
    NetworkFragment<FragmentFollowingBinding, FollowViewModel>(R.layout.fragment_following) {
    override val viewModel: FollowViewModel by lazy {
        FollowViewModel(appContainer.userRepository)
    }

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

    private val followAdapter = FollowListAdapter(onFollowClick = {
        if (it.followed) {
            viewModel.unfollow(it.userId)
        } else {
            viewModel.follow(it.userId)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.vm = viewModel
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            recyclerView.adapter = followAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFollowingList()
        }

        viewModel.getFollowingList()

    }

}