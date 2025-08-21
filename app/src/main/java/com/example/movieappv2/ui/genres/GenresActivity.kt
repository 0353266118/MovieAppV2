package com.example.movieappv2.ui.genres

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieappv2.databinding.ActivityGenresBinding
import com.example.movieappv2.ui.adapters.BaseMovieAdapter
import com.example.movieappv2.ui.adapters.GenreChipAdapter
import com.example.movieappv2.ui.detail.DetailActivity

class GenresActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenresBinding
    private val viewModel: GenresViewModel by viewModels()
    private lateinit var genreChipAdapter: GenreChipAdapter
    // SỬA LẠI ADAPTER Ở ĐÂY
    private lateinit var movieAdapter: BaseMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }

        setupRecyclerViews()
        observeViewModel()

        viewModel.selectGenre(-1)
    }

    private fun setupRecyclerViews() {
        // Cài đặt rv_genre_chips (giữ nguyên)
        genreChipAdapter = GenreChipAdapter { genre ->
            viewModel.selectGenre(genre.id)
        }
        binding.rvGenreChips.adapter = genreChipAdapter
        binding.rvGenreChips.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // SỬA LẠI PHẦN CÀI ĐẶT CHO rv_genre_movies
        // Sử dụng BaseMovieAdapter vì nó dùng layout item_top_search
        movieAdapter = BaseMovieAdapter { movie ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            startActivity(intent)
        }

        // Dùng LinearLayoutManager thay vì GridLayoutManager
        val layoutManager = LinearLayoutManager(this)
        binding.rvGenreMovies.apply {
            this.layoutManager = layoutManager // Gán layout manager mới
            this.adapter = movieAdapter

            // Thêm listener cuộn vô hạn (giữ nguyên)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!viewModel.isLoading && !viewModel.isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            viewModel.fetchMoviesForSelectedGenre()
                        }
                    }
                }
            })
        }
    }

    private fun observeViewModel() {
        // Lắng nghe danh sách thể loại
        viewModel.genres.observe(this) { genres ->
            genreChipAdapter.submitList(genres)
        }
        // Lắng nghe danh sách phim
        viewModel.moviesByGenre.observe(this) { movies ->
            movieAdapter.submitList(movies)
        }
    }
}