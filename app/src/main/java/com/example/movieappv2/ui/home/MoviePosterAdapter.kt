package com.example.movieappv2.ui.home // Sửa lại package cho đúng

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.databinding.ItemMoviePosterBinding
import com.example.movieappv2.ui.adapters.BannerAdapter
import com.example.movieappv2.utils.Constants

class MoviePosterAdapter(
    private val onMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, MoviePosterAdapter.MovieViewHolder>(BannerAdapter.DiffCallback) {

    inner class MovieViewHolder(private val binding: ItemMoviePosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            // binding.tvMovieGenre.text = movie.genre // Tạm ẩn đi

            val posterUrl = Constants.IMAGE_BASE_URL + movie.posterPath
            Glide.with(itemView.context).load(posterUrl).into(binding.ivPoster)

            itemView.setOnClickListener { onMovieClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMoviePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}