package com.youcancook.gobong.adapter.bindingAdapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.youcancook.gobong.R


@BindingAdapter("setProfileImageUrl")
fun ImageView.setProfileImageUrl(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.default_profile_img)
        .circleCrop()
        .into(this)
}

@BindingAdapter("setProfileImageByteArray")
fun setProfileImageByteArray(view: ImageView, data: ByteArray) {
    Glide.with(view.context)
        .load(data)
        .placeholder(R.drawable.default_profile_img)
        .circleCrop()
        .into(view)
}

@BindingAdapter("setImageUrl")
fun setImageUrl(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .placeholder(R.drawable.background_default_image)
        .into(view)
}

@BindingAdapter("setImageUrlWithEmpty")
fun setImageUrlWithEmpty(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .into(view)
}

@BindingAdapter("setImageByteArray")
fun setImageByteArray(view: ImageView, image: ByteArray?) {
    Glide.with(view.context)
        .load(image)
        .placeholder(R.drawable.thumbnail_add_placeholder)
        .into(view)
}

@BindingAdapter("setImageByteArrayWithEmpty")
fun setImageByteArrayWithEmpty(view: ImageView, image: ByteArray?) {
    Glide.with(view.context)
        .load(image)
        .into(view)
}

@BindingAdapter("setNecessaryTextView")
fun setNecessaryTextView(view: TextView, text: String) {
    val wordToSpan: Spannable =
        SpannableString(text)

    wordToSpan.setSpan(
        ForegroundColorSpan(view.context.resources.getColor(R.color.red)),
        text.length - 1,
        text.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    view.text = wordToSpan
}

@BindingAdapter("recipeStepTimeChip")
fun recipeStepTime(view: Chip, text: String) {
    val times = text.split(" ")
    view.text = if (times[1] == "0초") {
        times[0]
    } else if (times[0] == "0분") {
        times[1]
    } else {
        text
    }
}

@BindingAdapter("recipeStepTime")
fun recipeStepTime(view: TextView, text: String) {
    val times = text.split(" ")
    view.text = if (times[1] == "0초") {
        times[0]
    } else if (times[0] == "0분") {
        times[1]
    } else {
        text
    }
}

@BindingAdapter("toolText")
fun toolText(view: TextView, tools: List<String>) {
    view.text = if (tools.isEmpty()) {
        "-"
    } else if (tools.size == 1) {
        tools[0]
    } else {
        "${tools[0]}\n+${tools.size - 1}"
    }
}

@BindingAdapter("submitData")
fun <T> submitData(view: RecyclerView, data: List<T>?) {
    data ?: return

    val adapter = view.adapter as ListAdapter<T, RecyclerView.ViewHolder?>
    adapter.submitList(data)
}


