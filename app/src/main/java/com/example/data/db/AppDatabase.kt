package com.example.data.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.NetworkScanResult
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkScanDao {
    @Query("SELECT * FROM network_scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<NetworkScanResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: NetworkScanResult): Long

    @Query("DELETE FROM network_scans WHERE id = :id")
    suspend fun deleteScanById(id: Int)

    @Query("DELETE FROM network_scans")
    suspend fun clearAllScans()
}

@Database(entities = [NetworkScanResult::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun networkScanDao(): NetworkScanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wifiscan_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
