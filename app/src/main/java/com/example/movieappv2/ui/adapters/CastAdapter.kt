package com.example.movieappv2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil // Import DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappv2.R
import com.example.movieappv2.data.model.Cast
import com.example.movieappv2.databinding.ItemCastBinding
import com.example.movieappv2.utils.Constants

// adapter chỉ đến danh sách diễn viên ở detailActivity

// SỬA LẠI THAM CHIẾU Ở ĐÂY, TRỎ VÀO DiffCallback BÊN DƯỚI
class CastAdapter : ListAdapter<Cast, CastAdapter.CastViewHolder>(DiffCallback) {

    inner class CastViewHolder(private val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.tvCastName.text = cast.name

            // Dùng placeholder riêng cho avatar
            val photoUrl = Constants.IMAGE_BASE_URL + cast.profilePath
            Glide.with(itemView.context)
                .load(photoUrl)
                .placeholder(R.drawable.placeholder_avatar) // Cần tạo ảnh này
                .error(R.drawable.placeholder_avatar)       // Dùng chung ảnh lỗi
                .into(binding.ivCastPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // THÊM COMPANION OBJECT NÀY VÀO
    companion object DiffCallback : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            // ID của diễn viên thường là 'id', nhưng ở đây ta có thể dùng 'name' để phân biệt
            // vì trong một danh sách cast, tên thường không trùng lặp
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            // So sánh tất cả các thuộc tính của đối tượng
            return oldItem == newItem
        }
    }
}