package com.example.movieappv2.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.example.movieappv2.data.local.AppDatabase
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.data.model.MovieDetail
import com.example.movieappv2.data.repository.MovieRepository
import kotlinx.coroutines.launch
import android.util.Log
import com.example.movieappv2.data.model.Cast
import com.example.movieappv2.data.model.Review


class DetailViewModel(application: Application) : AndroidViewModel(application) {

    // THÊM LIVE DATA MỚI
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews
    private val repository: MovieRepository

    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail> = _movieDetail

    // THÊM LIVE DATA MỚI
    private val _cast = MutableLiveData<List<Cast>>()
    val cast: LiveData<List<Cast>> = _cast

    init {
        // ...
    }

    // Hàm này sẽ được gọi cùng lúc với fetchMovieDetails
    fun fetchMovieCredits(movieId: Int) {
        viewModelScope.launch {
            _cast.value = repository.getMovieCredits(movieId)
        }
    }
    fun fetchMovieReviews(movieId: Int) {
        viewModelScope.launch {
            _reviews.value = repository.getMovieReviews(movieId)
        }
    }

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    init {
        val movieDao = AppDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao)
    }

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            val detail = repository.getMovieDetails(movieId)
            detail?.let {
                _movieDetail.value = it
            }
        }
    }

    fun checkFavoriteStatus(movieId: Int) {
        viewModelScope.launch {
            _isFavorite.value = repository.isFavorite(movieId)
        }
    }

    fun toggleFavorite(movieDetail: MovieDetail) {
        viewModelScope.launch {
            val isCurrentlyFavorite = _isFavorite.value ?: false

            // Cập nhật việc tạo đối tượng Movie
            val movie = Movie(
                id = movieDetail.id,
                title = movieDetail.title,
                overview = movieDetail.overview,
                posterPath = movieDetail.posterPath,
                voteAverage = movieDetail.voteAverage,
                backdropPath = movieDetail.backdropPath
            )

            if (isCurrentlyFavorite) {
                repository.removeFavorite(movie)
                _isFavorite.value = false
            } else {
                repository.addFavorite(movie)
                _isFavorite.value = true
            }
        }
    }
}