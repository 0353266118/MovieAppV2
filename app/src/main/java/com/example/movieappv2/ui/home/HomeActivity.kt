package com.example.movieappv2.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieappv2.R
import com.example.movieappv2.data.model.Movie
import com.example.movieappv2.databinding.ActivityHomeBinding
import com.example.movieappv2.ui.detail.DetailActivity
import com.example.movieappv2.ui.favorites.FavoritesActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var popularMoviesAdapter: MoviePosterAdapter
    // Chúng ta có thể thêm adapter cho Top Searches sau
    // private lateinit var topSearchesAdapter: MoviePosterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        setupBottomNav() // Thêm hàm xử lý thanh điều hướng

        // Ra lệnh cho ViewModel lấy dữ liệu phim phổ biến
        homeViewModel.fetchPopularMovies()
    }

    // Hàm xử lý khi người dùng click vào một poster phim
    private fun handleMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("MOVIE_ID", movie.id)
        startActivity(intent)
    }

    // Cài đặt cho các RecyclerView
    private fun setupRecyclerView() {
        // Khởi tạo adapter cho danh sách phim phổ biến
        popularMoviesAdapter = MoviePosterAdapter(emptyList()) { movie ->
            handleMovieClick(movie)
        }

        binding.rvRecommended.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }

        // Tạm thời chưa cài đặt cho rv_top_searches
    }

    // Lắng nghe dữ liệu từ ViewModel
    private fun observeViewModel() {
        homeViewModel.popularMovies.observe(this) { movies ->
            popularMoviesAdapter.updateData(movies)
        }
    }

    // HÀM MỚI: Xử lý sự kiện click trên thanh điều hướng dưới cùng
    private fun setupBottomNav() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // Khi người dùng nhấn vào item có id là nav_favorites trong file menu
                R.id.nav_favorites -> {
                    // Tạo Intent và mở FavoritesActivity
                    val intent = Intent(this, FavoritesActivity::class.java)
                    startActivity(intent)
                    // Trả về true để báo rằng sự kiện đã được xử lý
                    // và để icon được highlight
                    true
                }

                // Em có thể thêm các case khác cho Home, Search... ở đây
                // R.id.nav_home -> { ... ; true }
                // R.id.nav_search -> { ... ; true }

                else -> false // Trả về false cho các item chưa được xử lý
            }
        }
    }
}