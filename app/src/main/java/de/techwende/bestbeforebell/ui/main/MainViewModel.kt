package de.techwende.bestbeforebell.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.techwende.bestbeforebell.data.repository.ProductRepository
import de.techwende.bestbeforebell.domain.model.Product
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val repository: ProductRepository
    ) : ViewModel() {
        val products = repository.products

        fun addProduct(product: Product) {
            viewModelScope.launch {
                repository.addProduct(product)
            }
        }

        fun removeProduct(product: Product) {
            viewModelScope.launch {
                repository.removeProduct(product)
            }
        }
    }
