package de.techwende.bestbeforebell.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    // bestBefore as epoch millis
    val bestBefore: Long
)
