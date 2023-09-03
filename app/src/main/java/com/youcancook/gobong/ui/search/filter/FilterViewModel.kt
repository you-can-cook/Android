package com.youcancook.gobong.ui.search.filter

import androidx.lifecycle.ViewModel
import com.youcancook.gobong.model.Filter
import com.youcancook.gobong.model.Tool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilterViewModel : ViewModel() {

    private val _searchInput = MutableStateFlow("")

    private val _sort = MutableStateFlow(RECENT)
    val sort: StateFlow<String> get() = _sort

    private val _level = MutableStateFlow("")
    val level: StateFlow<String> get() = _level

    private val _time = MutableStateFlow(30)
    val time: StateFlow<Int> get() = _time

    private val _star = MutableStateFlow("")
    val star: StateFlow<String> get() = _star

    private val _tools = MutableStateFlow(
        listOf(
            Tool("전자레인지", false, true),
            Tool("에어프라이어", false, true),
            Tool("오븐", false, true),
            Tool("가스레인지", false, true),
            Tool("믹서", false, true),
            Tool("커피포트", false, true),
            Tool("프라이팬", false, true)
        )
    )
    val tools: StateFlow<List<Tool>> get() = _tools

    fun setFilter(filter: Filter) {
        if (filter.isEmpty()) return

        setSearchWord(filter.searchWord)
        setSort(filter.sortType)
        setLevel(filter.level)
        setTime(filter.time.toInt())
        setStar(filter.star)
        checkTools(filter.tools)
    }

    fun setSearchWord(word: String) {
        _searchInput.value = word
    }

    fun setSort(sort: String) {
        _sort.value = sort
    }

    fun setLevel(level: String) {
        _level.value = level
    }

    fun setTime(time: Int) {
        _time.value = time
    }

    fun setStar(star: String) {
        _star.value = star
    }

    fun filterTools(searchInput: String) {
        _tools.value = if (searchInput.isEmpty()) {
            _tools.value.map {
                it.copy(isVisible = true)
            }
        } else {
            _tools.value.map {
                if (it.toolName.contains(searchInput)) it.copy(isVisible = true)
                else it.copy(isVisible = false)
            }

        }
    }

    fun checkTools(toolNames: List<String>) {
        _tools.value = _tools.value.map { tool ->
            if (tool.toolName in toolNames) tool.copy(isChecked = true)
            else if (tool.isVisible) {
                tool.copy(isChecked = false)
            } else {
                tool
            }
        }
    }

    fun reset() {
        _searchInput.value = ""
        _sort.value = ""
        _level.value = ""
        _time.value = 0
        _star.value = ""
        _tools.value = emptyList()
    }

    fun getFilter() = Filter(
        _searchInput.value,
        _sort.value,
        _level.value,
        _time.value.toString(),
        _star.value,
        _tools.value.filter { it.isChecked }.map { it.toolName }
    )

    companion object {
        val RECENT = "최신순"
        val POPULAR = "인기순"
        val EASY = "쉬워요"
        val NORMAL = "보통이에요"
        val HARD = "어려워요"
        val FIVE = "5"
        val FOUR = "4"
        val THREE = "3"
        val TWO = "2"
        val ONE = "1"
        val EMPTY = ""
    }
}