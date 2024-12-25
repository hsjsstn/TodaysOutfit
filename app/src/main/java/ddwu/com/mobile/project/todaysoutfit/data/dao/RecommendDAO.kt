package ddwu.com.mobile.project.todaysoutfit.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ddwu.com.mobile.project.todaysoutfit.data.entity.RecommendEntity

@Dao
interface RecommendDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendation(recommend: RecommendEntity)

    @Query("SELECT clothingSuggestion FROM recommendations WHERE recmdId = :recmdId")
    suspend fun getRecommendationById(recmdId: Int): String?

    @Query("SELECT * FROM recommendations")
    suspend fun getAllRecommendations(): List<RecommendEntity>
}