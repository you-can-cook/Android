package com.youcancook.gobong.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.youcancook.gobong.databinding.ItemGridCardBinding
import com.youcancook.gobong.databinding.ItemMainCardBinding
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.UserProfile
import com.youcancook.gobong.ui.detail.DetailActivity
import com.youcancook.gobong.ui.detail.DetailActivity.Companion.RECIPE_ID

class GridRecyclerViewListAdapter(
    var spanCount: Int,
    val onFollowClick: (UserProfile) -> Unit = {},
) :
    ListAdapter<Card, RecyclerView.ViewHolder>(diffUtil) {

    inner class GridViewHolder(val binding: ItemGridCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {

                val intent = Intent(binding.root.context, DetailActivity::class.java).putExtra(
                    RECIPE_ID, currentList[adapterPosition].id
                )
                binding.root.context.startActivity(intent)
            }
        }

        fun bind(item: Card) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            CardViewHolder(
                ItemMainCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), this,
                onFollowClick
            )
        } else {
            GridViewHolder(
                ItemGridCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GridViewHolder) {
            holder.bind(currentList[position])
        } else if (holder is CardViewHolder) {
            holder.bind(currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (spanCount == 1) {
            1
        } else if (spanCount == 3) {
            0
        } else {
            -1
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem == newItem
            }

        }
    }
}