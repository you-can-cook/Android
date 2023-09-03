package com.youcancook.gobong.adapter

import android.content.Intent
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.youcancook.gobong.databinding.ItemMainCardBinding
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.UserProfile
import com.youcancook.gobong.ui.detail.DetailActivity
import com.youcancook.gobong.ui.my.other.OthersActivity

class CardViewHolder(
    val binding: ItemMainCardBinding,
    val adapter: ListAdapter<*, *>,
    val onFollowClick: (UserProfile) -> Unit = {},
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.run {
            followingButton.setOnClickListener {
                onFollowClick((adapter.currentList[adapterPosition] as Card).user)
            }
            root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailActivity::class.java).putExtra(
                    DetailActivity.RECIPE_ID, (adapter.currentList[adapterPosition] as Card).id
                )
                binding.root.context.startActivity(intent)
            }
            toolTextView.setOnClickListener {
                MaterialAlertDialogBuilder(binding.root.context)
                    .setItems((adapter.currentList[adapterPosition] as Card).tools.toTypedArray()) { _, _ -> }
                    .show()
            }
            profileImageView.setOnClickListener {
                val intent = Intent(binding.root.context, OthersActivity::class.java)
                    .putExtra(
                        OthersActivity.USER_ID,
                        (adapter.currentList[adapterPosition] as Card).user.userId
                    )
                binding.root.context.startActivity(intent)
            }
        }
    }

    fun bind(item: Card) {
        binding.item = item
        binding.followingButton.text = if (item.user.followed) {
            "팔로잉"
        } else {
            "팔로우"
        }

    }
}