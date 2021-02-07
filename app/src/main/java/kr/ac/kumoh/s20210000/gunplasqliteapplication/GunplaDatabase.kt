package kr.ac.kumoh.s20210000.gunplasqliteapplication

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "mechanic")
data class Mechanic (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val model: String,
    val manufacturer: String,
    val armor: String,
    val height: Double,
    val weight: Double
)

@Dao
interface GunplaDao {
   @Query("select * from mechanic")
   fun getAllMechanic(): Flow<List<Mechanic>>

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun insert(mechanic: Mechanic)
}

@Database(entities = [Mechanic::class], version = 1,
    exportSchema = false)
abstract class GunplaDatabase : RoomDatabase() {
    abstract fun gunplaDao(): GunplaDao

    companion object {
        @Volatile
        private var INSTANCE: GunplaDatabase? = null

        fun getDatabase(context: Context): GunplaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GunplaDatabase::class.java,
                    "gunpla_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

class GunplaRepository(private val gunplaDao: GunplaDao) {
    val allMechanic: Flow<List<Mechanic>>
        = gunplaDao.getAllMechanic()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(mechanic: Mechanic) {
        gunplaDao.insert(mechanic)
    }
}