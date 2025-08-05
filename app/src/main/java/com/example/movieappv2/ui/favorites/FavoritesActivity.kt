package com.example.movieappv2.ui.favorites

import android.content.Intent // 1. IMPORT INTENT
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieappv2.databinding.ActivityFavoritesBinding
import com.example.movieappv2.ui.detail.DetailActivity // 2. IMPORT DETAILACTIVITY

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        // 3. Sửa lại phần khởi tạo Adapter để cung cấp cả 2 hành động
        favoritesAdapter = FavoritesAdapter(
            // Hành động 1: Khi nhấn nút trái tim -> Xóa phim
            onFavoriteClick = { movie ->
                favoritesViewModel.removeFavorite(movie)
                Toast.makeText(this, "'${movie.title}' đã được xóa", Toast.LENGTH_SHORT).show()
            },
            // Hành động 2: Khi nhấn vào item -> Mở chi tiết
            onItemClick = { movie ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("MOVIE_ID", movie.id)
                startActivity(intent)
            }
        )

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = favoritesAdapter
        }
    }

    private fun observeViewModel() {
        favoritesViewModel.allFavorites.observe(this) { favoriteMovies ->
            favoritesAdapter.submitList(favoriteMovies)
        }
    }
}