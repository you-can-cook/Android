package com.youcancook.gobong.ui.search.filter

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.google.android.material.chip.Chip
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.ActivityFilterBinding
import com.youcancook.gobong.model.Filter
import com.youcancook.gobong.ui.base.GoBongActivity
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.EASY
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.EMPTY
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.FIVE
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.FOUR
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.HARD
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.NORMAL
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.ONE
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.POPULAR
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.RECENT
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.THREE
import com.youcancook.gobong.ui.search.filter.FilterViewModel.Companion.TWO

class FilterActivity : GoBongActivity<ActivityFilterBinding>(R.layout.activity_filter) {
    private val viewModel: FilterViewModel by viewModels()

    private val filterBottomSheet = FilterToolBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            vm = viewModel
        }

        setOldFilter(intent.getSerializableExtra(OLD_DATA) as Filter)

        binding.run {
            toolbar.setNavigationOnClickListener {
                finish()
            }

            resetButton.setOnClickListener {
                viewModel.reset()
                resetAllView()
            }

            searchButton.setOnClickListener {
                setResult(
                    RESULT_OK,
                    Intent().putExtra(
                        FILTER_DATA,
                        viewModel.getFilter()
                    )
                )
                finish()
            }
        }

        initListeners()
    }

    private fun setOldFilter(filter: Filter) {
        if (filter.isEmpty()) return

        viewModel.setFilter(filter)

        setViewByFilter(filter)
    }

    private fun setViewByFilter(filter: Filter) {
        binding.run {
            searchView.setQuery(filter.searchWord, false)
            if (filter.sortType.isNotEmpty()) {
                when (filter.sortType) {
                    RECENT -> sortToggleGroup.check(R.id.sortRecentTextView)
                    POPULAR -> sortToggleGroup.check(R.id.sortPopularTextView)
                }
            }

            if (filter.level.isNotEmpty()) {
                when (filter.level) {
                    EASY -> levelGroup.check(R.id.easyChip)
                    NORMAL -> levelGroup.check(R.id.normalChip)
                    HARD -> levelGroup.check(R.id.hardChip)
                }
            }

            if (filter.time.isNotEmpty()) {
                timeSlider.value = filter.time.toFloat()
            }

            if (filter.star.isNotEmpty()) {
                when (filter.star) {
                    FIVE -> starGroup.check(R.id.fiveStarChip)
                    FOUR -> starGroup.check(R.id.fourStarChip)
                    THREE -> starGroup.check(R.id.threeStarChip)
                    TWO -> starGroup.check(R.id.twoStarChip)
                    ONE -> starGroup.check(R.id.oneStarChip)
                }
            }
        }
    }

    private fun resetAllView() {
        binding.run {
            searchView.setQuery("", false)

            sortToggleGroup.clearChecked()

            levelGroup.clearCheck()

            timeSlider.value = 30f

            starGroup.clearCheck()
        }
    }

    private fun initListeners() {
        binding.run {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText ?: return true
                    viewModel.setSearchWord(newText)
                    return true
                }

            })

            sortToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->

                when (checkedId) {
                    R.id.sortRecentTextView -> {
                        if (isChecked) viewModel.setSort(RECENT)
                        else viewModel.setSort(EMPTY)
                    }

                    R.id.sortPopularTextView -> {
                        if (isChecked) viewModel.setSort(POPULAR)
                        else viewModel.setSort(EMPTY)
                    }
                }
            }

            levelGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                if (checkedIds.isEmpty()) {
                    viewModel.setLevel(EMPTY)
                    return@setOnCheckedStateChangeListener
                }

                when (checkedIds[0]) {
                    R.id.easyChip -> viewModel.setLevel(EASY)
                    R.id.normalChip -> viewModel.setLevel(NORMAL)
                    R.id.hardChip -> viewModel.setLevel(HARD)
                }
            }

            timeSlider.addOnSliderTouchListener(object : OnSliderTouchListener {

                override fun onStartTrackingTouch(slider: Slider) {}

                override fun onStopTrackingTouch(slider: Slider) {
                    viewModel.setTime(timeSlider.value.toInt())
                }

            })

            starGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                if (checkedIds.isEmpty()) {
                    viewModel.setStar(EMPTY)
                    return@setOnCheckedStateChangeListener
                }

                when (checkedIds[0]) {
                    R.id.fiveStarChip -> viewModel.setStar(FIVE)
                    R.id.fourStarChip -> viewModel.setStar(FOUR)
                    R.id.threeStarChip -> viewModel.setStar(THREE)
                    R.id.twoStarChip -> viewModel.setStar(TWO)
                    R.id.oneStarChip -> viewModel.setStar(ONE)
                }
            }

            showMoreToolsButton.setOnClickListener {
                filterBottomSheet.show(supportFragmentManager, FilterToolBottomSheet.TAG)
            }

            toolsGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                val views = checkedIds.map {
                    group.findViewById<Chip>(it).text.toString()
                }
                viewModel.checkTools(views)
            }
        }
    }

    companion object {
        val OLD_DATA = "oldData"
        val FILTER_DATA = "filter"
    }
}