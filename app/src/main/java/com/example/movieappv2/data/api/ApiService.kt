package com.example.movieappv2.data.api

import com.example.movieappv2.data.model.CreditsResponse
import com.example.movieappv2.data.model.GenreResponse
import com.example.movieappv2.data.model.MovieDetail
import com.example.movieappv2.data.model.MovieResponse
import com.example.movieappv2.data.model.ReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// interface định nghĩa các lệnh API đến trang TMDB
interface ApiService {


    @GET("genre/movie/list")
    suspend fun getMovieGenres(@Query("api_key") apiKey: String): Response<GenreResponse>

    @GET("discover/movie")
    suspend fun discoverMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): Response<MovieResponse>


    //Lấy danh sách phim được đánh giá cao
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieResponse>


    // THÊM HÀM MỚI
    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<ReviewResponse>

    // HÀM MỚI: Lấy danh sách phim thịnh hành trong ngày
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    // HÀM MỚI: Tìm kiếm phim theo từ khóa
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Response<MovieResponse>


    // Lấy danh sách phim phổ biến
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int // Thêm tham số trang
    ): Response<MovieResponse>

    // THÊM HÀM MỚI
    @GET("movie/{movie_id}") // Dùng {movie_id} để truyền ID vào đường dẫn
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int, // Annotation @Path để thay thế {movie_id}
        @Query("api_key") apiKey: String
    ): Response<MovieDetail> // Trả về đối tượng MovieDetail


    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<CreditsResponse> // Trả về đối tượng CreditsResponse
}

