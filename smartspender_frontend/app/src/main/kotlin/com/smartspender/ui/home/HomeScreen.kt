package com.smartspender.ui.home

import android.app.Application
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartspender.data.db.TransactionEntity
import com.smartspender.data.repo.Totals
import com.smartspender.data.repo.TransactionRepository
import com.smartspender.ui.theme.OceanBackground
import com.smartspender.ui.theme.OceanText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = TransactionRepository(app)

    val totals = repo.totals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Totals(0.0, 0.0, 0.0))

    val recent = repo.allTransactions()
        .map { it.take(8) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@Composable
// PUBLIC_INTERFACE
fun HomeScreen(vm: HomeViewModel) {
    val totals by vm.totals.collectAsState()
    val recent by vm.recent.collectAsState()

    Surface(color = OceanBackground) {
        // Render minimal Text-only content to avoid inline container composables
        Text(
            text = "Overview",
            style = MaterialTheme.typography.titleLarge,
            color = OceanText,
        )

        Text("Total Balance", style = MaterialTheme.typography.titleMedium, color = OceanText)
        Text("£${"%.2f".format(totals.balance)}", style = MaterialTheme.typography.bodyLarge, color = OceanText)

        Text("Income", style = MaterialTheme.typography.titleMedium, color = OceanText)
        Text("£${"%.2f".format(totals.income)}", style = MaterialTheme.typography.bodyLarge, color = OceanText)

        Text("Expenses", style = MaterialTheme.typography.titleMedium, color = OceanText)
        Text("£${"%.2f".format(totals.expenses)}", style = MaterialTheme.typography.bodyLarge, color = OceanText)

        Text(text = "Recent Transactions", style = MaterialTheme.typography.titleMedium, color = OceanText)
        Divider()
        for (tx in recent) {
            TransactionRow(tx)
        }
    }
}

@Composable
private fun TransactionRow(tx: TransactionEntity) {
    val sign = if (tx.type == "income") "+" else "-"
    Text(
        text = "${tx.category} • ${tx.date}",
        style = MaterialTheme.typography.bodyMedium,
        color = OceanText
    )
    Text(
        text = "$sign£${"%.2f".format(tx.amount)}${if (!tx.notes.isNullOrBlank()) " — ${tx.notes}" else ""}",
        style = MaterialTheme.typography.bodyLarge,
        color = OceanText
    )
    Divider()
}
