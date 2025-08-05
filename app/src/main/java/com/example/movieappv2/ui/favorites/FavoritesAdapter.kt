package com.example.movieappv2.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.databinding.ItemTopSearchBinding
import com.example.movieappv2.ui.adapters.BaseMovieAdapter
import com.example.movieappv2.utils.Constants

class FavoritesAdapter(
    // Tham số cũ, dùng để xử lý click nút trái tim (XÓA)
    private val onFavoriteClick: (Movie) -> Unit,
    // THÊM THAM SỐ MỚI, dùng để xử lý click vào item (MỞ CHI TIẾT)
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, FavoritesAdapter.FavoriteViewHolder>(BaseMovieAdapter.DiffCallback) { // Có thể dùng lại DiffCallback của BaseMovieAdapter

    inner class FavoriteViewHolder(private val binding: ItemTopSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            binding.tvMovieGenre.text = movie.overview?.substring(0, minOf(movie.overview.length, 50)) + "..."

            val posterUrl = Constants.IMAGE_BASE_URL + movie.posterPath
            Glide.with(itemView.context).load(posterUrl).into(binding.ivPoster)

            // Gán sự kiện cho nút trái tim (giữ nguyên)
            binding.ivItemFavorite.setOnClickListener { onFavoriteClick(movie) }

            // THÊM SỰ KIỆN MỚI CHO TOÀN BỘ ITEM VIEW
            itemView.setOnClickListener {
                onItemClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemTopSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}