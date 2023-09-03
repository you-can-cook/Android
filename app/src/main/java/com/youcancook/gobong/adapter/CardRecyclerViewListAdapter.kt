package com.youcancook.gobong.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.youcancook.gobong.databinding.ItemMainCardBinding
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.UserProfile

class CardRecyclerViewListAdapter(
    val onFollowClick: (UserProfile) -> Unit,
) :
    ListAdapter<Card, CardViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
            ItemMainCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            this@CardRecyclerViewListAdapter,
            onFollowClick
        )
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem == newItem
            }

        }
    }
}