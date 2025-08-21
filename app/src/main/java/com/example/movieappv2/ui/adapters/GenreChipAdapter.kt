package com.example.movieappv2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieappv2.data.model.Genre
import com.example.movieappv2.databinding.ItemGenreChipBinding
import com.google.android.material.chip.Chip // Import Chip

class GenreChipAdapter(
    private val onGenreClick: (Genre) -> Unit
) : ListAdapter<Genre, GenreChipAdapter.GenreViewHolder>(DiffCallback) {

    private var selectedPosition = 0

    // Sửa lại ViewHolder để nó hiểu binding.root là một Chip
    inner class GenreViewHolder(private val binding: ItemGenreChipBinding) : RecyclerView.ViewHolder(binding.root) {
        // Ép kiểu binding.root thành Chip để truy cập các thuộc tính của nó
        private val chip: Chip = binding.root

        fun bind(genre: Genre) {
            // SỬA LẠI Ở ĐÂY: Dùng biến 'chip' đã được ép kiểu
            chip.text = genre.name
            chip.isChecked = (adapterPosition == selectedPosition)

            chip.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (selectedPosition != adapterPosition) {
                        // Thông báo cho item cũ bỏ highlight
                        notifyItemChanged(selectedPosition)
                        // Cập nhật vị trí mới
                        selectedPosition = adapterPosition
                        // Highlight item mới
                        notifyItemChanged(selectedPosition)
                        // Báo cho Activity biết genre nào đã được chọn
                        onGenreClick(genre)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
            return oldItem == newItem
        }
    }
}