package de.techwende.bestbeforebell.ui.main

import androidx.lifecycle.ViewModel
import de.techwende.bestbeforebell.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class MainViewModel : ViewModel() {
    private val _products =
        MutableStateFlow(
            listOf(
                Product(1, "Milk", LocalDate.now().plusDays(2)),
                Product(2, "Cheese", LocalDate.now().plusWeeks(1)),
            ),
        )

    val products = _products.asStateFlow()
}
