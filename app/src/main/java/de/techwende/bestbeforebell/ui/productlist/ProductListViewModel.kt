package de.techwende.bestbeforebell.ui.productlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.techwende.bestbeforebell.domain.model.Product
import de.techwende.bestbeforebell.domain.service.ProductService
import kotlinx.coroutines.flow.MutableStateFlow
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
        private val productService: ProductService,
        savedStateHandle: SavedStateHandle
    ) : ViewModel() {
        val products: StateFlow<List<Product>> =
            productService
                .observeProducts()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )

        private val productId: Long? = savedStateHandle["productId"]
        val editingProduct: StateFlow<Product?> =
            productId
                ?.let { id ->
                    productService
                        .findById(id)
                        .stateIn(
                            viewModelScope,
                            SharingStarted.WhileSubscribed(5_000),
                            null
                        )
                }
                ?: MutableStateFlow(null)

        fun saveProduct(
            name: String,
            bestBefore: LocalDate,
            quantity: Int = 1
        ) {
            viewModelScope.launch {
                val product =
                    editingProduct.value?.copy(
                        name = name,
                        bestBefore = bestBefore,
                        quantity = quantity
                    ) ?: Product(
                        id = 0,
                        name = name,
                        bestBefore = bestBefore,
                        quantity = quantity
                    )

                productService.saveProduct(product)
            }
        }

        fun removeProduct(product: Product) {
            viewModelScope.launch {
                productService.removeProduct(product)
            }
        }
    }
