package com.hayawanat.app.db

import androidx.room.*
import com.hayawanat.app.model.GameMode
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "scores")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameMode: String,
    val difficulty: Int,
    val score: Int,
    val total: Int,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface ScoreDao {
    @Insert
    suspend fun insert(score: ScoreEntity)

    @Query("SELECT * FROM scores ORDER BY timestamp DESC LIMIT 50")
    fun getAll(): Flow<List<ScoreEntity>>

    @Query("SELECT MAX(score) FROM scores WHERE gameMode = :mode")
    fun getBestScore(mode: String): Flow<Int?>

    @Query("SELECT COUNT(*) FROM scores")
    fun getTotalGames(): Flow<Int>
}

@Database(entities = [ScoreEntity::class], version = 1, exportSchema = false)
abstract class HayawanatDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile private var INSTANCE: HayawanatDatabase? = null

        fun getInstance(context: android.content.Context): HayawanatDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    HayawanatDatabase::class.java,
                    "hayawanat_db"
                ).build().also { INSTANCE = it }
            }
    }
}
