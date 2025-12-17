package de.techwende.bestbeforebell.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.techwende.bestbeforebell.domain.model.Product
import de.techwende.bestbeforebell.domain.service.ProductService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
    @Inject
    constructor(
        private val productService: ProductService
    ) : ViewModel() {
        val products: StateFlow<List<Product>> =
            productService
                .observeProducts()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )

        fun addProduct(
            name: String,
            bestBefore: LocalDate
        ) {
            viewModelScope.launch {
                productService.addProduct(name, bestBefore)
            }
        }

        fun addDummyProduct() {
            viewModelScope.launch {
                productService.addProduct(
                    name = "Milk",
                    bestBefore = LocalDate.now().plusDays(3)
                )
            }
        }

        fun removeProduct(product: Product) {
            viewModelScope.launch {
                productService.removeProduct(product)
            }
        }
    }
