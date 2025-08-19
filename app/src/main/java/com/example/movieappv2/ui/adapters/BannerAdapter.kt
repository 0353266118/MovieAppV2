package com.example.movieappv2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.databinding.ItemBannerMovieBinding
import com.example.movieappv2.utils.Constants

class BannerAdapter(
    private val onBannerClick: (Movie) -> Unit
) : ListAdapter<Movie, BannerAdapter.BannerViewHolder>(DiffCallback) {

    inner class BannerViewHolder(private val binding: ItemBannerMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            // Dùng backdropPath cho banner để có ảnh ngang
            val bannerUrl = Constants.IMAGE_BASE_URL + movie.backdropPath
            Glide.with(itemView.context)
                .load(bannerUrl)
                .into(binding.ivBannerImage)

            // Gán dữ liệu text vào các TextView trong banner
            binding.tvBannerTitle.text = "Watch popular movies ${movie.title}"

            itemView.setOnClickListener { onBannerClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}