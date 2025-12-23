package de.techwende.bestbeforebell.domain.service

import de.techwende.bestbeforebell.domain.model.Product
import de.techwende.bestbeforebell.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductService
    @Inject
    constructor(
        private val repository: ProductRepository
    ) {
        fun observeProducts(): Flow<List<Product>> = repository.products

        fun findById(id: Long) = repository.findById(id)

        suspend fun saveProduct(product: Product) {
            if (repository.findById(product.id).firstOrNull() != null) {
                repository.updateProduct(product)
                return
            }

            val result = repository.findByNameAndBestBefore(product.name, product.bestBefore)
            if (result != null) {
                repository.updateProduct(result.copy(quantity = result.quantity + product.quantity))
            } else {
                repository.addProduct(product)
            }
        }

        suspend fun removeProduct(product: Product) {
            val result = repository.findById(product.id).firstOrNull() ?: return
            if (result.quantity > 1) {
                repository.updateProduct(result.copy(quantity = result.quantity - 1))
            } else {
                repository.removeProduct(product)
            }
        }

        suspend fun removeAllOfProduct(product: Product) {
            repository.removeProduct(product)
        }
    }
