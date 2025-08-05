package com.example.movieappv2.ui.favorites

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieappv2.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // =================== THÊM CODE XỬ LÝ CLICK VÀO ĐÂY ===================
        // Gán sự kiện click cho ImageView có id là "iv_back"
        binding.ivBack.setOnClickListener {
            // Lệnh finish() sẽ đóng Activity hiện tại và quay lại màn hình trước đó
            finish()
        }
        // =====================================================================

        setupRecyclerView()
        observeViewModel()
    }

    // Các hàm setupRecyclerView() và observeViewModel() giữ nguyên như cũ
    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter { movie ->
            favoritesViewModel.removeFavorite(movie)
            Toast.makeText(this, "'${movie.title}' đã được xóa", Toast.LENGTH_SHORT).show()
        }
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