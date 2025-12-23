package de.techwende.bestbeforebell.ui.editproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.techwende.bestbeforebell.R
import de.techwende.bestbeforebell.ui.productlist.ProductListViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun ProductEditorScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    onFinished: () -> Unit
) {
    val editingProduct by viewModel.editingProduct.collectAsStateWithLifecycle()
    var name by remember(editingProduct) { mutableStateOf(editingProduct?.name ?: "") }
    var isNameError by remember(editingProduct) { mutableStateOf(false) }
    var quantity by remember(editingProduct) {
        mutableStateOf(
            editingProduct?.quantity?.toString() ?: "1"
        )
    }
    var isQuantityError by remember(editingProduct) { mutableStateOf(false) }
    var bestBefore by remember(editingProduct) {
        mutableStateOf(
            editingProduct?.bestBefore ?: LocalDate.now().plusDays(8)
        )
    }
    val datePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis =
                bestBefore
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
        )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.add_product),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )

        OutlinedTextField(
            value = name,
            onValueChange = { input -> name = input },
            label = { Text(stringResource(R.string.product_name)) },
            singleLine = true,
            isError = isNameError,
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (isNameError) {
                    Text(stringResource(R.string.invalid_name))
                }
            },
        )

        OutlinedTextField(
            value = quantity,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    quantity = input
                }
            },
            label = { Text(stringResource(R.string.quantity)) },
            singleLine = true,
            isError = isQuantityError,
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (isQuantityError) {
                    Text(stringResource(R.string.invalid_number))
                }
            },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        DatePicker(
            state = datePickerState,
            title = { Text(stringResource(R.string.best_before)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                val selectedDate =
                    Instant
                        .ofEpochMilli(datePickerState.selectedDateMillis ?: 0L)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                val quantityInt = quantity.toIntOrNull() ?: 0
                isQuantityError = quantity.isNotEmpty() && quantityInt < 1
                name = name.trim()
                isNameError = name.isEmpty()

                if (!isQuantityError && !isNameError) {
                    viewModel.saveProduct(name, selectedDate, quantityInt)
                    onFinished()
                }
            }) {
                Text(stringResource(R.string.save))
            }

            Button(onClick = {
                onFinished()
            }) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}
