package com.example.movieappv2.ui.search

import android.app.Application
import androidx.lifecycle.*
import com.example.movieappv2.data.local.AppDatabase
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.data.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    init {
        val movieDao = AppDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao)
        // Tải danh sách thịnh hành ngay khi ViewModel được tạo
        fetchTrendingMovies()
    }

    fun fetchTrendingMovies() = viewModelScope.launch {
        _searchResults.value = repository.getTrendingMovies()
    }

    fun searchMovies(query: String) = viewModelScope.launch {
        if (query.isBlank()) {
            fetchTrendingMovies() // Nếu ô tìm kiếm trống, hiển thị lại danh sách thịnh hành
        } else {
            _searchResults.value = repository.searchMovies(query)
        }
    }
}