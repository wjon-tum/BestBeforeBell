package de.techwende.bestbeforebell.domain.repository

import de.techwende.bestbeforebell.data.dao.ProductDao
import de.techwende.bestbeforebell.data.model.ProductEntity
import de.techwende.bestbeforebell.domain.model.Product
import de.techwende.bestbeforebell.util.dateToMillis
import de.techwende.bestbeforebell.util.millisToDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class ProductRepository(
    private val dao: ProductDao
) {
    val products: Flow<List<Product>> =
        dao
            .getAllProducts()
            .map { list -> list.map { it.toDomain() } }

    suspend fun addProduct(product: Product) = dao.insert(product.toEntity())

    suspend fun updateProduct(product: Product) = dao.update(product.toEntity())

    suspend fun removeProduct(product: Product) = dao.delete(product.toEntity())

    suspend fun findByNameAndBestBefore(
        name: String,
        date: LocalDate
    ) = dao.findByNameAndDate(name.lowercase(), dateToMillis(date))?.toDomain()

    fun findById(id: Long) = dao.findById(id).map { it?.toDomain() }

    suspend fun findByName(name: String) =
        dao.findByName(name).map { list -> list.map { it.toDomain() } }

    fun ProductEntity.toDomain(): Product =
        Product(
            id = id,
            name = name.replaceFirstChar { c -> c.uppercase() },
            quantity = quantity,
            bestBefore = millisToDate(bestBefore)
        )

    fun Product.toEntity(): ProductEntity =
        ProductEntity(
            id = id,
            name = name.lowercase(),
            quantity = quantity,
            bestBefore = dateToMillis(bestBefore)
        )
}
