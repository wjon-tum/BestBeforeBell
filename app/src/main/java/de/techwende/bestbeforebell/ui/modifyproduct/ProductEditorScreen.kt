package de.techwende.bestbeforebell.ui.modifyproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import de.techwende.bestbeforebell.ui.productlist.ProductListViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun ProductEditorScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    productId: Long?,
    onFinished: () -> Unit
) {
    val editingProduct by viewModel.editingProduct.collectAsState()
    var name by remember { mutableStateOf(editingProduct?.name ?: "") }
    var bestBefore by remember { mutableStateOf(editingProduct?.bestBefore ?: LocalDate.now()) }

    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis =
                bestBefore
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
        )

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DatePicker(
            state = datePickerState,
            title = { Text("Best Before") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                val selectedDate =
                    Instant
                        .ofEpochMilli(datePickerState.selectedDateMillis ?: 0L)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                viewModel.saveProduct(name, selectedDate)
                onFinished()
            }) {
                Text("Save")
            }

            Button(onClick = {
                viewModel.cancelEditing()
                onFinished()
            }) {
                Text("Cancel")
            }
        }
    }
}
