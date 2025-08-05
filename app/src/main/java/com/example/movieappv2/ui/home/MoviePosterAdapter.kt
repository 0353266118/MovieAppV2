package com.example.movieappv2.ui.home // Sửa lại package cho đúng

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Import Glide
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.databinding.ItemMoviePosterBinding
import com.example.movieappv2.utils.Constants

class MoviePosterAdapter(
    private var movieList: List<Movie>,
    private val onMovieClick: (Movie) -> Unit

) : RecyclerView.Adapter<MoviePosterAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemMoviePosterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {


        val binding = ItemMoviePosterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.binding.tvMovieTitle.text = movie.title
//        holder.binding.tvMovieGenre.text = movie.genre

        // Dùng Glide để tải ảnh từ URL
        val fullPosterUrl = "https://image.tmdb.org/t/p/w500" + movie.posterPath
        Glide.with(holder.itemView.context)
            .load(fullPosterUrl)
            .into(holder.binding.ivPoster)
        holder.itemView.setOnClickListener {
            onMovieClick(movie)
        }

    }

    override fun getItemCount(): Int = movieList.size

    fun updateData(newMovieList: List<Movie>) {
        movieList = newMovieList
        notifyDataSetChanged()
    }
}