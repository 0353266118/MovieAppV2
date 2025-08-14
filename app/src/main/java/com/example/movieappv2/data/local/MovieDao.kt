package com.example.movieappv2.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movieappv2.data.model.Movie

// DAO định nghĩa các hành động với dữ liệu từ local database

@Dao // Đánh dấu đây là một DAO
interface MovieDao {

    // Thêm một phim vào danh sách yêu thích.
    // onConflict = OnConflictStrategy.REPLACE: Nếu thêm phim đã có sẵn (cùng id), nó sẽ được thay thế.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(movie: Movie) // Dùng suspend vì đây là thao tác I/O, cần chạy trên coroutine

    // Xóa một phim khỏi danh sách yêu thích
    @Delete
    suspend fun removeFavorite(movie: Movie)

    // Lấy tất cả các phim yêu thích và trả về dưới dạng LiveData.
    // LiveData sẽ tự động cập nhật UI khi danh sách yêu thích thay đổi.
    @Query("SELECT * FROM favorite_movies")
    fun getAllFavorites(): LiveData<List<Movie>>

    // Kiểm tra xem một phim đã có trong danh sách yêu thích chưa, dựa vào ID
    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    suspend fun getFavoriteById(movieId: Int): Movie?
}