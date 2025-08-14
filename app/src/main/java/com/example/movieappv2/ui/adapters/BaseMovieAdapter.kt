package com.example.movieappv2.ui.adapters // Package mới

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.databinding.ItemTopSearchBinding // Dùng layout này
import com.example.movieappv2.utils.Constants

// adapter chỉ đến danh sách phim sau khi thực hiện tìm kiếm
class BaseMovieAdapter(
    private val onMovieClick: (Movie) -> Unit // Lambda để báo cáo sự kiện click
) : ListAdapter<Movie, BaseMovieAdapter.MovieViewHolder>(DiffCallback) {

    inner class MovieViewHolder(private val binding: ItemTopSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            binding.tvMovieGenre.text = movie.overview?.substring(0, minOf(movie.overview.length, 50)) + "..." // Hiển thị 50 ký tự đầu

            val posterUrl = Constants.IMAGE_BASE_URL + movie.posterPath
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(binding.ivPoster)

            // Gán sự kiện click cho toàn bộ item
            itemView.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemTopSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
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