package com.example.movieappv2.data.repository

import androidx.lifecycle.LiveData
import com.example.movieappv2.data.api.RetrofitInstance
import com.example.movieappv2.data.local.MovieDao
import com.example.movieappv2.data.model.Cast
import com.example.movieappv2.data.model.Genre
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.data.model.MovieDetail
import com.example.movieappv2.data.model.Review
import com.example.movieappv2.utils.Constants

// nơi chọn xem lấy dữ liệu từ API hay ROOM local

class MovieRepository(private val movieDao: MovieDao) {
    suspend fun getMovieGenres(): List<Genre> {
        val response = RetrofitInstance.api.getMovieGenres(Constants.API_KEY)
        return if (response.isSuccessful) response.body()?.genres ?: emptyList() else emptyList()
    }

    suspend fun discoverMoviesByGenre(genreId: Int, page: Int): List<Movie> {
        val response = RetrofitInstance.api.discoverMoviesByGenre(Constants.API_KEY, genreId, page)
        return if (response.isSuccessful) response.body()?.movies ?: emptyList() else emptyList()
    }


    suspend fun getTopRatedMovies(page: Int): List<Movie> {
        val response = RetrofitInstance.api.getTopRatedMovies(Constants.API_KEY, page)
        return if (response.isSuccessful) response.body()?.movies ?: emptyList() else emptyList()
    }
    suspend fun getPopularMovies(page: Int): List<Movie> {
        val response = RetrofitInstance.api.getPopularMovies(Constants.API_KEY, page)
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
    // THÊM HÀM MỚI
    suspend fun getMovieReviews(movieId: Int): List<Review> {
        val response = RetrofitInstance.api.getMovieReviews(movieId, Constants.API_KEY)
        return if (response.isSuccessful) {
            response.body()?.reviews ?: emptyList()
        } else {
            emptyList()
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
    // HÀM MỚI
    suspend fun getTrendingMovies(): List<Movie> {
        val response = RetrofitInstance.api.getTrendingMovies(Constants.API_KEY)
        return if (response.isSuccessful) response.body()?.movies ?: emptyList() else emptyList()
    }

    // HÀM MỚI
    suspend fun searchMovies(query: String): List<Movie> {
        val response = RetrofitInstance.api.searchMovies(Constants.API_KEY, query)
        return if (response.isSuccessful) response.body()?.movies ?: emptyList() else emptyList()
    }

    suspend fun getMovieCredits(movieId: Int): List<Cast> {
        val response = RetrofitInstance.api.getMovieCredits(movieId, Constants.API_KEY)
        return if (response.isSuccessful) {
            response.body()?.cast ?: emptyList()
        } else {
            emptyList()
        }
    }
}