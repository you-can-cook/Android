package com.youcancook.gobong.ui.my

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.youcancook.gobong.R
import com.youcancook.gobong.adapter.GridItemDecorator
import com.youcancook.gobong.adapter.GridRecyclerViewListAdapter
import com.youcancook.gobong.databinding.FragmentMyBinding
import com.youcancook.gobong.ui.base.NetworkFragment
import com.youcancook.gobong.ui.base.NetworkStateListener
import com.youcancook.gobong.ui.my.follow.FollowActivity
import com.youcancook.gobong.ui.my.follow.FollowActivity.Companion.IS_FOLLOWER
import com.youcancook.gobong.ui.my.follow.FollowActivity.Companion.NICKNAME
import com.youcancook.gobong.ui.my.setting.SettingActivity

class MyFragment : NetworkFragment<FragmentMyBinding, MyProfileViewModel>(R.layout.fragment_my) {

    override val viewModel: MyProfileViewModel by lazy {
        MyProfileViewModel(appContainer.goBongRepository)
    }

    private val gridAdapter = GridRecyclerViewListAdapter(3)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            vm = viewModel
        }

        super.onViewCreated(view, savedInstanceState)

        gridItemDecorator.also {
            it.margin =
                requireContext().resources.getDimension(R.dimen.grid_margin).toInt()
        }

        binding.run {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            recyclerView.addItemDecoration(gridItemDecorator)
            recyclerView.adapter = gridAdapter
        }

        initListeners()

    }

    private fun initListeners() {
        binding.run {

            toolbar.setNavigationOnClickListener {
                if (it.isSelected) {
                    setGridRecyclerView()
                } else {
                    setLinearRecyclerView()
                }
                it.isSelected = it.isSelected.not()
            }

            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.toolbar_my_setting) {
                    val intent = Intent(requireContext(), SettingActivity::class.java)
                    startActivity(intent)
                }
                return@setOnMenuItemClickListener true
            }

            setGridRecyclerView()

            followerView.setOnClickListener {
                val intent = Intent(requireContext(), FollowActivity::class.java)
                    .putExtra(IS_FOLLOWER, true)
                    .putExtra(NICKNAME,viewModel.getMyNickname())
                startActivity(intent)
            }

            followingView.setOnClickListener {
                val intent = Intent(requireContext(), FollowActivity::class.java)
                    .putExtra(IS_FOLLOWER, false)
                    .putExtra(NICKNAME,viewModel.getMyNickname())
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getMyInfo()
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