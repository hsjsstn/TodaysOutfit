package ddwu.com.mobile.project.todaysoutfit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ddwu.com.mobile.project.todaysoutfit.data.dao.RecommendDAO
import ddwu.com.mobile.project.todaysoutfit.data.entity.RecommendEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [RecommendEntity::class], version = 1, exportSchema = false)
abstract class RecommendDatabase : RoomDatabase() {

    abstract fun recommendDAO(): RecommendDAO

    companion object {
        @Volatile
        private var INSTANCE: RecommendDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): RecommendDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecommendDatabase::class.java,
                    "recommend_database"
                )
                    .addCallback(RecommendDatabaseCallback(scope)) // Callback 추가
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class RecommendDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    database.populateDatabase(database.recommendDAO())
                }
            }
        }
    }

    suspend fun populateDatabase(recommendDAO: RecommendDAO) {
        // 초기 데이터 삽입
        recommendDAO.insertRecommendation(RecommendEntity(0, 28.0, 100.0, "반소매 티셔츠와\n반바지가 좋겠어요!\n민소매 옷도 좋아요.\n썬크림 꼭 바르세요!"))
        recommendDAO.insertRecommendation(RecommendEntity(1, 23.0, 28.0, "반소매 티셔츠와\n반바지가좋겠어요!\n얇은 셔츠나 면바지도\n좋을 것 같네요."))
        recommendDAO.insertRecommendation(RecommendEntity(2, 20.0, 23.0, "긴소매 티셔츠와\n면바지가 좋겠어요!\n블라우스나 슬랙스도\n좋아요."))
        recommendDAO.insertRecommendation(RecommendEntity(3, 17.0, 20.0, "맨투맨이나 후드티에\n긴 바지가 좋겠어요!\n니트에 가디건도\n좋아요."))
        recommendDAO.insertRecommendation(RecommendEntity(4, 12.0, 17.0,"자켓이나 가디건\n하나 챙겨 입는 것이\n좋은 날이에요!\n아래에는 청바지도\n좋을 것 같네요"))
        recommendDAO.insertRecommendation(RecommendEntity(5, 9.0, 12.0,  "코트 입기 좋은 날이네요!\n기모 바지도 좋아요!\n점퍼나 야상도\n꺼내기 좋아요."))
        recommendDAO.insertRecommendation(RecommendEntity(6, 5.0, 9.0, "추위를 많이 탄다면\n히트텍은 필수예요!\n기모가 들어간 옷을\n입는 것이 좋겠어요."))
        recommendDAO.insertRecommendation(RecommendEntity(7, -100.0, 5.0, "패딩은 필수!\n안에도 따뜻하게\n기모가 들어간 옷을\n입어주세요!\n목도리도 좋아요"))
    }
}