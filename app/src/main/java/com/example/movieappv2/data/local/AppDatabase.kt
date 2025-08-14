package com.example.movieappv2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieappv2.data.model.Movie

// class kết nối Entity và DAO

// Annotation @Database để khai báo các entity và phiên bản
@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Cung cấp một hàm để lấy DAO
    abstract fun movieDao(): MovieDao

    // Dùng Companion Object để tạo một instance duy nhất của Database cho toàn bộ app (Singleton)
    companion object {
        @Volatile // Đảm bảo instance luôn được cập nhật trên mọi luồng
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Chỉ tạo instance nếu nó chưa tồn tại để tránh lãng phí
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_database" // Tên file database sẽ được tạo
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}