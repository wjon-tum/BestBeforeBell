package de.techwende.bestbeforebell.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import de.techwende.bestbeforebell.domain.model.Product

@Preview
@Composable
fun ProductListScreen(viewModel: MainViewModel = hiltViewModel()) {
    val products by viewModel.products.collectAsState(emptyList())

    LazyColumn {
        items(products) { product ->
            ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
    ) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "Best before: ${product.bestBefore}",
        )
    }
}
