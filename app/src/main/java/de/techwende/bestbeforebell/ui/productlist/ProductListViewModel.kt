package de.techwende.bestbeforebell.ui.productlist

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

    private val _editingProduct = MutableStateFlow<Product?>(null)
    val editingProduct: StateFlow<Product?> = _editingProduct

    fun startEditing(product: Product?) {
        _editingProduct.value = product
    }

    fun saveProduct(
        name: String,
        bestBefore: LocalDate,
        multiplicity: Int = 1
    ) {
        viewModelScope.launch {
            val product =
                _editingProduct.value?.copy(
                    name = name,
                    bestBefore = bestBefore,
                    multiplicity = multiplicity
                ) ?: Product(
                    id = 0,
                    name = name,
                    bestBefore = bestBefore,
                    multiplicity = multiplicity
                )

            productService.addProduct(product.name, product.bestBefore)
            _editingProduct.value = null
        }
    }

    fun cancelEditing() {
        _editingProduct.value = null
    }

        fun removeProduct(product: Product) {
            viewModelScope.launch {
                productService.removeProduct(product)
            }
        }
    }
