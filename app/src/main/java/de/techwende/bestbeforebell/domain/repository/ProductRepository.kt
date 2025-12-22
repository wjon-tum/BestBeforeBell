package de.techwende.bestbeforebell.domain.repository

import de.techwende.bestbeforebell.data.dao.ProductDao
import de.techwende.bestbeforebell.data.model.ProductEntity
import de.techwende.bestbeforebell.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId

class ProductRepository(
    private val dao: ProductDao
) {
    val products: Flow<List<Product>> =
        dao
            .getAllProducts()
            .map { list -> list.map { it.toDomain() } }

    suspend fun addProduct(product: Product) = dao.insert(product.toEntity())

    suspend fun removeProduct(product: Product) = dao.delete(product.toEntity())

    fun ProductEntity.toDomain(): Product =
        Product(
            id = id,
            name = name,
            multiplicity = multiplicity,
            bestBefore =
                Instant
                    .ofEpochMilli(bestBefore)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
        )

    fun Product.toEntity(): ProductEntity =
        ProductEntity(
            id = id,
            name = name,
            multiplicity = multiplicity,
            bestBefore = bestBefore.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
}
