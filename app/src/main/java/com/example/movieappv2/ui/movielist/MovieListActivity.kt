package com.example.movieappv2.ui.movielist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieappv2.databinding.ActivityMovieListBinding
import com.example.movieappv2.ui.adapters.BaseMovieAdapter
import com.example.movieappv2.ui.detail.DetailActivity

class MovieListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieListBinding
    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieAdapter: BaseMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lớp ActivityMovieListBinding giờ đã tồn tại
        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gán sự kiện cho nút quay lại
        binding.ivBack.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        movieAdapter = BaseMovieAdapter { movie ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvMovieList.layoutManager = layoutManager
        binding.rvMovieList.adapter = movieAdapter

        // Thêm Listener để xử lý cuộn vô hạn
        binding.rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLoading && !viewModel.isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        viewModel.fetchPopularMovies()
                    }
                }
            }
        })
    }

    private fun observeViewModel() {
        viewModel.movies.observe(this) { movies ->
            movieAdapter.submitList(movies)
        }
    }
}