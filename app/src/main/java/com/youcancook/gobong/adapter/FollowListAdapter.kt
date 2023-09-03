package com.youcancook.gobong.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.youcancook.gobong.databinding.ItemFollowBinding
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.User
import com.youcancook.gobong.model.UserProfile
import com.youcancook.gobong.ui.detail.DetailActivity
import com.youcancook.gobong.ui.my.other.OthersActivity

class FollowListAdapter(
    val onFollowClick: (UserProfile) -> Unit = {},

    ) : ListAdapter<UserProfile, FollowListAdapter.FollowViewHolder>(diffUtil) {

    inner class FollowViewHolder(val binding: ItemFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.profileImageView.setOnClickListener {
                val intent = Intent(binding.root.context, OthersActivity::class.java).putExtra(
                    OthersActivity.USER_ID, (currentList[adapterPosition] as UserProfile).userId
                )
                binding.root.context.startActivity(intent)
            }

            binding.followingButton.setOnClickListener {
                onFollowClick((currentList[adapterPosition] as UserProfile))
                binding.followingButton.isSelected = binding.followingButton.isSelected.not()
            }

        }

        fun bind(data: UserProfile) {
            binding.item = data
            binding.followingButton.text = if (data.followed) {
                "팔로잉"
            } else {
                "팔로우"
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FollowListAdapter.FollowViewHolder {
        return FollowViewHolder(
            ItemFollowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FollowListAdapter.FollowViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UserProfile>() {
            override fun areItemsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
                return oldItem == newItem
            }

        }
    }

}