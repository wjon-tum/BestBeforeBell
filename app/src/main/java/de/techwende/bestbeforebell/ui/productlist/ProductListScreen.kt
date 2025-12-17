package de.techwende.bestbeforebell.ui.productlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import de.techwende.bestbeforebell.domain.model.Product

@Preview
@Composable
fun ProductListScreen(viewModel: ProductListViewModel = hiltViewModel(LocalViewModelStoreOwner.current!!)) {
    val products by viewModel.products.collectAsState(emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.addDummyProduct() }
            ) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            items(products) { product ->
                ProductItem(product, viewModel)
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    viewModel: ProductListViewModel
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { viewModel.removeProduct(product) }
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
