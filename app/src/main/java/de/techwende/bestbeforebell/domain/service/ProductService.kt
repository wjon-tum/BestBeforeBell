package de.techwende.bestbeforebell.domain.service

import de.techwende.bestbeforebell.domain.model.Product
import de.techwende.bestbeforebell.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductService
    @Inject
    constructor(
        private val repository: ProductRepository
    ) {
        fun observeProducts(): Flow<List<Product>> = repository.products

        suspend fun addProduct(
            name: String,
            bestBefore: LocalDate
        ) {
            repository.addProduct(
                Product(
                    id = 0,
                    name = name,
                    bestBefore = bestBefore
                )
            )
        }

        suspend fun removeProduct(product: Product) {
            repository.removeProduct(product)
        }
    }
