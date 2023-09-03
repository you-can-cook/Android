package com.youcancook.gobong.ui.my.follow

import android.os.Bundle
import android.view.View
import com.youcancook.gobong.R
import com.youcancook.gobong.adapter.FollowListAdapter
import com.youcancook.gobong.databinding.FragmentFollowerBinding
import com.youcancook.gobong.ui.base.NetworkFragment
import com.youcancook.gobong.ui.base.NetworkStateListener

class FollowerFragment :
    NetworkFragment<FragmentFollowerBinding, FollowViewModel>(R.layout.fragment_follower) {
    private val followAdapter = FollowListAdapter(onFollowClick = {
        if (it.followed) {
            viewModel.unfollow(it.userId)
        } else {
            viewModel.follow(it.userId)
        }
    })
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.vm = viewModel
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            recyclerView.adapter = followAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFollowerList()
        }

        viewModel.getFollowerList()
    }
}