package com.example.movieappv2.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.example.movieappv2.data.local.AppDatabase
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.data.model.MovieDetail
import com.example.movieappv2.data.repository.MovieRepository
import kotlinx.coroutines.launch
import android.util.Log


class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository

    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail> = _movieDetail

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
            val movie = Movie(
                id = movieDetail.id,
                title = movieDetail.title,
                overview = movieDetail.overview,
                posterPath = movieDetail.posterPath,
                voteAverage = movieDetail.voteAverage
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