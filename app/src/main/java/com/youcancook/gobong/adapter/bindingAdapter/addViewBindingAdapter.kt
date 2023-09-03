package com.youcancook.gobong.adapter.bindingAdapter

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.youcancook.gobong.R
import com.youcancook.gobong.model.Tool

@BindingAdapter("ingredientChips")
fun <T> ingredientChips(view: ChipGroup, data: List<String>) {

    //removeOldView
    view.removeAllViews()

    for (dt in data) {
        view.addView(Chip(view.context, null, R.attr.IngredientChips).apply {
            text = dt
        })
    }
}

@BindingAdapter("recipeStepTools")
fun <T> recipeStepTools(view: ChipGroup, data: List<String>) {

    //removeOldView
    val childCount = view.childCount
    view.removeViews(1, childCount - 1)

    for (dt in data) {
        view.addView(Chip(view.context, null, R.attr.RecipeStepTools).apply {
            text = dt
        })
    }
}

@BindingAdapter("filterChips")
fun <T> ChipGroup.filterChips(filterChips: List<Tool>) {

    //removeOldView
    removeAllViews()

    for (dt in filterChips.filter { it.isVisible }) {
        addView(Chip(context, null, R.attr.FilterChips).apply {
            text = dt.toolName
            isChecked = dt.isChecked
        })
    }
}

@BindingAdapter("selectedChips")
fun <T> ChipGroup.selectedChips(selectedChips: List<Tool>) {
    //removeOldView
    removeViews(0, childCount - 1)

    println("Selected ${selectedChips.joinToString(" ")}")
    for (dt in selectedChips.filter { it.isChecked }) {
        addView(Chip(context, null, R.attr.FilterChips).apply {
            text = dt.toolName
            isChecked = true
            isVisible = dt.isVisible
        }, childCount - 1)
    }
}

fun FlexboxLayout.addIngredient(data: String) {

    val dp = resources.displayMetrics.density.toInt()
    val ViewGrouplayoutParams = FlexboxLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    ViewGrouplayoutParams.setMargins(0, 0, 4 * dp, 0)

    addView(Chip(context, null, R.attr.IngredientAddedChips).apply {
        text = data
        layoutParams = ViewGrouplayoutParams
        setOnCloseIconClickListener {
            this@addIngredient.removeView(it)
        }
    }, childCount - 2)
}

