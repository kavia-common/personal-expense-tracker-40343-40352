package com.smartspender.ui.transactions

import android.app.Application
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartspender.data.db.TransactionEntity
import com.smartspender.data.repo.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionsViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = TransactionRepository(app)

    private val typeFilter: MutableStateFlow<String?> = MutableStateFlow(null)
    private val categoryFilter: MutableStateFlow<String?> = MutableStateFlow(null)

    // Combine current filter values with the full list, then filter in memory.
    // This avoids using flatMapLatest and eliminates ambiguous destructuring.
    val items: StateFlow<List<TransactionEntity>> =
        combine(
            repo.allTransactions(), // Flow<List<TransactionEntity>>
            typeFilter as StateFlow<String?>,
            categoryFilter as StateFlow<String?>
        ) { list: List<TransactionEntity>, t: String?, c: String? ->
            list.filter { tx ->
                (t == null || tx.type == t) && (c == null || tx.category == c)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setTypeFilter(v: String?) { viewModelScope.launch { typeFilter.emit(v) } }
    fun setCategoryFilter(v: String?) { viewModelScope.launch { categoryFilter.emit(v) } }

    fun currentType(): String = typeFilter.value ?: ""
    fun currentCategory(): String = categoryFilter.value ?: ""
}

@Composable
// PUBLIC_INTERFACE
fun TransactionsScreen(vm: TransactionsViewModel) {
    val list by vm.items.collectAsState()

    Surface {
        Text("Filters")
        OutlinedTextField(
            value = vm.currentType(),
            onValueChange = { vm.setTypeFilter(it.ifBlank { null }) },
            label = { Text("Type (income/expense or empty)") }
        )
        OutlinedTextField(
            value = vm.currentCategory(),
            onValueChange = { vm.setCategoryFilter(it.ifBlank { null }) },
            label = { Text("Category (or empty)") }
        )

        Text("Transactions")
        Text("—")
        for (tx in list) {
            TxRow(tx)
        }
    }
}

@Composable
private fun TxRow(tx: TransactionEntity) {
    val sign = if (tx.type == "income") "+" else "-"
    Text("${tx.date} • ${tx.category} • $sign£${"%.2f".format(tx.amount)}${if (!tx.notes.isNullOrBlank()) " • ${tx.notes}" else ""}")
}
