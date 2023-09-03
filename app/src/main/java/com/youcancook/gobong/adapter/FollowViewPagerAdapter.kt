package com.youcancook.gobong.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.youcancook.gobong.ui.my.follow.FollowerFragment
import com.youcancook.gobong.ui.my.follow.FollowingFragment

class FollowViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            FollowerFragment()
        } else {
            FollowingFragment()
        }
    }


}