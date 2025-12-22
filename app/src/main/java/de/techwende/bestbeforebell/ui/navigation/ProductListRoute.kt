package de.techwende.bestbeforebell.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import de.techwende.bestbeforebell.ui.modifyproduct.ProductEditorScreen
import de.techwende.bestbeforebell.ui.productlist.ProductListScreen
import de.techwende.bestbeforebell.ui.productlist.ProductListViewModel

@Composable
fun ProductListRoute(viewModel: ProductListViewModel = hiltViewModel()) {
    val showEditor = remember { mutableStateOf(false) }

    if (showEditor.value) {
        ProductEditorScreen(
            productId = null,
            onFinished = { showEditor.value = false }
        )
    } else {
        ProductListScreen(
            onAddClicked = {
                viewModel.startEditing(null)
                showEditor.value = true
            },
            onEditClicked = { product ->
                viewModel.startEditing(product)
                showEditor.value = true
            }
        )
    }
}
