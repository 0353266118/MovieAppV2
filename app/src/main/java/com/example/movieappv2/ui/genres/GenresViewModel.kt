package com.example.movieappv2.ui.genres

import android.app.Application
import androidx.lifecycle.*
import com.example.movieappv2.data.local.AppDatabase
import com.example.movieappv2.data.model.Genre
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.data.repository.MovieRepository
import kotlinx.coroutines.launch

class GenresViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    val genres = MutableLiveData<List<Genre>>()
    val moviesByGenre = MutableLiveData<List<Movie>>()
    val selectedGenreId = MutableLiveData<Int>()

    // === THÊM CÁC BIẾN QUẢN LÝ TRẠNG THÁI ===
    private var currentPage = 1
    var isLoading = false
    var isLastPage = false
    // =======================================

    init {
        val movieDao = AppDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao)
        fetchGenres()
    }

    private fun fetchGenres() = viewModelScope.launch {
        val genreList = repository.getMovieGenres()
        genres.value = listOf(Genre(id = -1, name = "All")) + genreList
    }

    // Khi người dùng chọn một thể loại MỚI
    fun selectGenre(genreId: Int) {
        if (selectedGenreId.value == genreId) return
        selectedGenreId.value = genreId
        // Reset lại mọi thứ cho lần tải đầu tiên của thể loại mới
        resetAndFetchMovies()
    }

    // Hàm reset trạng thái
    private fun resetAndFetchMovies() {
        currentPage = 1
        isLastPage = false
        moviesByGenre.value = emptyList() // Xóa danh sách cũ
        fetchMoviesForSelectedGenre()
    }

    // Hàm này giờ có thể được gọi nhiều lần để tải các trang tiếp theo
    fun fetchMoviesForSelectedGenre() {
        if (isLoading || isLastPage) return

        isLoading = true
        viewModelScope.launch {
            val currentGenreId = selectedGenreId.value ?: -1

            val newMovies = if (currentGenreId == -1) {
                repository.getPopularMovies(currentPage)
            } else {
                repository.discoverMoviesByGenre(currentGenreId, currentPage)
            }

            if (newMovies.isNotEmpty()) {
                val currentList = moviesByGenre.value ?: emptyList()
                moviesByGenre.value = currentList + newMovies
                currentPage++
            } else {
                isLastPage = true
            }
            isLoading = false
        }
    }
}