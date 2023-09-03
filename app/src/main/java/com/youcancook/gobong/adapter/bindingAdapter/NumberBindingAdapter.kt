package com.youcancook.gobong.adapter.bindingAdapter

import android.widget.EditText
import androidx.databinding.BindingAdapter


@BindingAdapter("time")
fun time(view: EditText, time: String) {
    view.setText(
        if (time.length < 2) {
            time.padStart(2, '0')
        } else {
            time
        }
    )
}