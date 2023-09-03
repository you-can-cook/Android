package com.youcancook.gobong.ui.my.follow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.youcancook.gobong.adapter.FollowViewPagerAdapter
import com.youcancook.gobong.databinding.ActivityFollowBinding

class FollowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFollowBinding
    private val viewPagerAdapter = FollowViewPagerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {

            backArrowButton.setOnClickListener {
                finish()
            }

            viewPager.adapter = viewPagerAdapter

            val nickname = intent?.getStringExtra(NICKNAME)
            nickname?.let {
                binding.nicknameTextView.text = it
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = if (position == 0) {
                    "팔로워"
                } else {
                    "팔로잉"
                }
            }.attach()

        }

        val isFollower = intent?.getBooleanExtra(IS_FOLLOWER, true)
        isFollower?.let {
            if (it.not()) {
                binding.viewPager.currentItem = 1

            }
        }


    }

    companion object {
        const val IS_FOLLOWER = "isFollower"
        const val NICKNAME = "nickname"
    }
}