package com.example.movieappv2.ui.home

import android.app.Application // 1. Import Application
import androidx.lifecycle.AndroidViewModel // 2. Import AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieappv2.data.local.AppDatabase // 3. Import AppDatabase
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.data.repository.MovieRepository
import kotlinx.coroutines.launch

// 4. Sửa class signature: Kế thừa từ AndroidViewModel và nhận application
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _bannerMovies = MutableLiveData<List<Movie>>()
    val bannerMovies: LiveData<List<Movie>> = _bannerMovies



    // 5. Khai báo repository
    private val repository: MovieRepository

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> = _popularMovies

    // 6. Dùng khối init để khởi tạo
    init {
        // Lấy dao từ database thông qua application context
        val movieDao = AppDatabase.getDatabase(application).movieDao()
        // Khởi tạo repository và truyền dao vào -> HẾT LỖI
        repository = MovieRepository(movieDao)
    }

    fun fetchInitialData() {
        viewModelScope.launch {
            // 1. Tải phim Top Rated cho Banner
            val topRatedMovies = repository.getTopRatedMovies(page = 1)
            // Lấy 5 phim đầu tiên để hiển thị trên banner
            _bannerMovies.value = topRatedMovies.take(5)

            // 2. Tải phim Popular cho danh sách Recommended
            val popularMoviesList = repository.getPopularMovies(page = 1)
            _popularMovies.value = popularMoviesList
        }
    }

}