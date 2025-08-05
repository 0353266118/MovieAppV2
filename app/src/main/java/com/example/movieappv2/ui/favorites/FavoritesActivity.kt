package com.example.movieappv2.ui.favorites

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieappv2.databinding.ActivityFavoritesBinding

class FavoritesActivity : AppCompatActivity() {

    // Sử dụng ViewBinding cho Activity này
    private lateinit var binding: ActivityFavoritesBinding

    // Khởi tạo ViewModel
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    // Khai báo Adapter
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Khởi tạo và thiết lập ViewBinding
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gọi các hàm cài đặt
        setupRecyclerView()
        observeViewModel()
    }

    // Hàm này để cài đặt cho RecyclerView
    private fun setupRecyclerView() {
        // Khởi tạo adapter
        favoritesAdapter = FavoritesAdapter { movie ->
            // Đây là đoạn code sẽ chạy khi người dùng nhấn vào trái tim trong danh sách
            favoritesViewModel.removeFavorite(movie)
            Toast.makeText(this, "'${movie.title}' đã được xóa khỏi Yêu thích", Toast.LENGTH_SHORT).show()
        }

        // Gán LayoutManager và Adapter cho RecyclerView
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = favoritesAdapter
        }
    }

    // Hàm này để lắng nghe dữ liệu từ ViewModel
    private fun observeViewModel() {
        // Lắng nghe danh sách các phim yêu thích
        // Bất cứ khi nào danh sách trong database thay đổi (thêm/xóa),
        // LiveData sẽ tự động gửi danh sách mới về đây.
        favoritesViewModel.allFavorites.observe(this) { favoriteMovies ->
            // Cập nhật danh sách mới cho adapter
            // ListAdapter sẽ tự động tính toán và cập nhật giao diện rất hiệu quả
            favoritesAdapter.submitList(favoriteMovies)
        }
    }
}