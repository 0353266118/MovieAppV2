package com.example.movieappv2.ui.movielist

import android.app.Application
import androidx.lifecycle.*
import com.example.movieappv2.data.local.AppDatabase
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    // Biến để theo dõi trang hiện tại
    private var currentPage = 1
    // Biến để tránh gọi API liên tục khi đang tải
    var isLoading = false
    // Biến để biết đã hết phim để tải chưa
    var isLastPage = false

    init {
        val movieDao = AppDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao)
        // Tải trang đầu tiên ngay khi ViewModel được tạo
        fetchPopularMovies()
    }

    fun fetchPopularMovies() {
        if (isLoading || isLastPage) return // Nếu đang tải hoặc đã hết trang thì không làm gì

        isLoading = true
        viewModelScope.launch {
            val newMovies = repository.getPopularMovies(currentPage)
            if (newMovies.isNotEmpty()) {
                // Lấy danh sách cũ và cộng thêm danh sách mới
                val currentList = _movies.value ?: emptyList()
                _movies.value = currentList + newMovies
                currentPage++ // Tăng số trang lên cho lần gọi tiếp theo
            } else {
                isLastPage = true // Nếu API trả về danh sách rỗng, tức là đã hết phim
            }
            isLoading = false
        }
    }
}