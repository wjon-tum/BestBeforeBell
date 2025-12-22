package de.techwende.bestbeforebell.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.techwende.bestbeforebell.data.model.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY bestBefore ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query(
        """
        SELECT * FROM products
        WHERE name = :name AND bestBefore = :bestBefore
        LIMIT 1
    """
    )
    suspend fun findByNameAndDate(
        name: String,
        bestBefore: Long
    ): ProductEntity?

    @Query(
        """
        SELECT * FROM products
        WHERE id = :id
    """
    )
    fun findById(id: Long): Flow<ProductEntity?>

    @Query(
        """
        SELECT * FROM products
        WHERE name = :name
    """
    )
    fun findByName(name: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity): Long

    @Update
    suspend fun update(product: ProductEntity): Int

    @Delete
    suspend fun delete(product: ProductEntity): Int
}
