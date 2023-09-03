package com.youcancook.gobong.ui.my.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.youcancook.gobong.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}