package com.example.movieappv2.data.repository

import androidx.lifecycle.LiveData
import com.example.movieappv2.data.api.RetrofitInstance
import com.example.movieappv2.data.local.MovieDao
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.data.model.MovieDetail
import com.example.movieappv2.utils.Constants

class MovieRepository(private val movieDao: MovieDao) {
    suspend fun getPopularMovies(): List<Movie> {
        val response = RetrofitInstance.api.getPopularMovies(Constants.API_KEY)
        return if (response.isSuccessful) {
            response.body()?.movies ?: emptyList()
        } else {
            emptyList()
        }
    }
    suspend fun getMovieDetails(movieId: Int): MovieDetail? {
        val response = RetrofitInstance.api.getMovieDetails(movieId, Constants.API_KEY)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
    // ===== CÁC HÀM MỚI TƯƠNG TÁC VỚI ROOM =====

    suspend fun addFavorite(movie: Movie) {
        movieDao.addFavorite(movie)
    }

    suspend fun removeFavorite(movie: Movie) {
        movieDao.removeFavorite(movie)
    }

    fun getAllFavorites(): LiveData<List<Movie>> {
        return movieDao.getAllFavorites()
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return movieDao.getFavoriteById(movieId) != null
    }
}