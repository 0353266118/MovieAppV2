package com.example.movieappv2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil // Import DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieappv2.data.model.Review
import com.example.movieappv2.databinding.ItemReviewBinding


class ReviewAdapter : ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(DiffCallback) {

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.tvReviewAuthor.text = review.author
            binding.tvReviewContent.text = review.content

            // RatingBar nhận giá trị Float.
            // Dùng elvis operator (?:) để cung cấp giá trị mặc định 0.0 nếu rating là null.
            binding.rbReviewRating.rating = (review.authorDetails.rating ?: 0.0).toFloat()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            // Mỗi review có một tác giả và nội dung, ta có thể kết hợp chúng để tạo ID duy nhất
            // Hoặc đơn giản là dựa vào nội dung nếu không có ID từ API
            return oldItem.author == newItem.author && oldItem.createdAt == newItem.createdAt
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            // So sánh tất cả các thuộc tính của đối tượng
            return oldItem == newItem
        }
    }
}