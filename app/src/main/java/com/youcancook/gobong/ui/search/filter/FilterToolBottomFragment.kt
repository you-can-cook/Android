package com.youcancook.gobong.ui.search.filter

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.BottomSheetFilterSearchBinding
import com.youcancook.gobong.ui.base.GoBongFragment

class FilterToolBottomFragment :
    GoBongFragment<BottomSheetFilterSearchBinding>(R.layout.bottom_sheet_filter_search) {

    val viewModel: FilterViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.run {
            vm = viewModel
        }

        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.run {
            backArrowButton.setOnClickListener {
                (requireParentFragment() as FilterToolBottomSheet).dismiss()
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText ?: return true
                    viewModel.filterTools(newText)
                    return true
                }

            })

            allToolsGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                val views = checkedIds.map {
                    group.findViewById<Chip>(it).text.toString()
                }
                viewModel.checkTools(views)
            }
        }
    }
}