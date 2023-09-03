package com.youcancook.gobong.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.youcancook.gobong.R
import com.youcancook.gobong.adapter.GridItemDecorator
import com.youcancook.gobong.adapter.GridRecyclerViewListAdapter
import com.youcancook.gobong.databinding.FragmentSearchBinding
import com.youcancook.gobong.model.Filter
import com.youcancook.gobong.ui.base.NetworkFragment
import com.youcancook.gobong.ui.base.NetworkStateListener
import com.youcancook.gobong.ui.search.filter.FilterActivity
import com.youcancook.gobong.ui.search.filter.FilterActivity.Companion.OLD_DATA

class SearchFragment :
    NetworkFragment<FragmentSearchBinding, SearchViewModel>(R.layout.fragment_search) {
    private var filterActivityLauncher: ActivityResultLauncher<Intent>? = null

    override val viewModel: SearchViewModel by lazy {
        SearchViewModel(appContainer.goBongRepository, appContainer.userRepository)
    }

    private val gridAdapter = GridRecyclerViewListAdapter(3, onFollowClick = {
        if (it.followed) {
            viewModel.unfollow(it.userId)
        } else {
            viewModel.follow(it.userId)
        }
    })

    private val gridItemDecorator = GridItemDecorator()
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

            toggleButton.setOnClickListener {
                if (it.isSelected) {
                    setGridRecyclerView()
                } else {
                    setLinearRecyclerView()
                }
                it.isSelected = it.isSelected.not()
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.filter(it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })

            filterButton.setOnClickListener {
                Intent(requireContext(), FilterActivity::class.java).run {
                    putExtra(OLD_DATA, viewModel.getCurrentFilter())
                    filterActivityLauncher?.launch(this)
                }
            }

            swipeRefresh.setOnRefreshListener {
                viewModel.getAllRecipes()
            }
            setGridRecyclerView()
        }

        filterActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val filterData =
                        result.data?.getSerializableExtra(FilterActivity.FILTER_DATA) as Filter
                    viewModel.setFilter(filterData)
                }
            }

        viewModel.getAllRecipes()
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