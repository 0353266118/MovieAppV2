package com.example.movieappv2.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.databinding.ItemTopSearchBinding
import com.example.movieappv2.utils.Constants

class FavoritesAdapter(
    private val onFavoriteClick: (Movie) -> Unit
) : ListAdapter<Movie, FavoritesAdapter.FavoriteViewHolder>(DiffCallback) {

    inner class FavoriteViewHolder(private val binding: ItemTopSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        // Hàm bind này sẽ nhận một đối tượng Movie và gán dữ liệu vào các view
        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            binding.tvMovieGenre.text = movie.overview // Hiển thị tạm overview

            val posterUrl = Constants.IMAGE_BASE_URL + movie.posterPath
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(binding.ivPoster)

            // Gán sự kiện click cho nút trái tim
            binding.ivItemFavorite.setOnClickListener {
                onFavoriteClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemTopSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movie = getItem(position)
        // Gọi hàm bind để gán dữ liệu
        holder.bind(movie)
    }

    // DiffUtil giúp RecyclerView cập nhật một cách thông minh
    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}