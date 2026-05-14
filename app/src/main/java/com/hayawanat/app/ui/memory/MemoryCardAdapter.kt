package com.hayawanat.app.ui.memory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hayawanat.app.R
import com.hayawanat.app.databinding.ItemMemoryCardBinding

class MemoryCardAdapter(
    private val onCardClick: (Int) -> Unit
) : ListAdapter<MemoryCard, MemoryCardAdapter.CardViewHolder>(DiffCallback) {

    inner class CardViewHolder(private val binding: ItemMemoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(card: MemoryCard, position: Int) {
            if (card.isMatched) {
                binding.tvCardContent.text = card.content
                binding.root.setBackgroundResource(R.drawable.bg_card_matched)
                binding.root.isClickable = false
            } else if (card.isFlipped) {
                binding.tvCardContent.text = card.content
                binding.root.setBackgroundResource(R.drawable.bg_card_flipped)
                binding.root.isClickable = false
            } else {
                binding.tvCardContent.text = "?"
                binding.root.setBackgroundResource(R.drawable.bg_card_back)
                binding.root.setOnClickListener { onCardClick(position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CardViewHolder(
            ItemMemoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) =
        holder.bind(getItem(position), position)

    companion object DiffCallback : DiffUtil.ItemCallback<MemoryCard>() {
        override fun areItemsTheSame(a: MemoryCard, b: MemoryCard) = a.id == b.id
        override fun areContentsTheSame(a: MemoryCard, b: MemoryCard) = a == b
    }
}
