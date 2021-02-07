package kr.ac.kumoh.s20210000.gunplasqliteapplication

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "mechanic")
data class Mechanic (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val model: String,
    val manufacturer: String?,
    val armor: String?,
    val height: Double?,
    val weight: Double?
)

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE `mechanic2` (
                `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                `name` TEXT NOT NULL,
                `model` TEXT NOT NULL,
                `manufacturer` TEXT,
                `armor` TEXT,
                `height` REAL,
                `weight` REAL
            )
        """)
        database.execSQL("""
            INSERT INTO mechanic2 SELECT * FROM mechanic;
        """)
        database.execSQL("""
            DROP TABLE mechanic;
        """)
        database.execSQL("""
            ALTER TABLE mechanic2 RENAME TO mechanic;
        """)
    }
}

@Dao
interface GunplaDao {
   @Query("select * from mechanic")
   fun getAllMechanic(): Flow<List<Mechanic>>

   @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun insert(mechanic: Mechanic)
}

@Database(entities = [Mechanic::class], version = 2,
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
                ).addMigrations(MIGRATION_1_2).build()
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