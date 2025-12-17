package de.techwende.bestbeforebell.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import de.techwende.bestbeforebell.data.dao.ProductDao
import de.techwende.bestbeforebell.data.model.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
