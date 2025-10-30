package com.smartspender.ui.dashboard

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartspender.data.db.AppDatabase
import com.smartspender.ui.theme.OceanBackground
import com.smartspender.ui.theme.OceanText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).transactionDao()
    val byCat = dao.expensesByCategory().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@Composable
// PUBLIC_INTERFACE
fun DashboardScreen(vm: DashboardViewModel) {
    val data by vm.byCat.collectAsState()
    val total = data.sumOf { it.total }.coerceAtLeast(0.0001)

    Surface(color = OceanBackground) {
        // Render title and each item as separate Text calls to avoid inline container composables
        Text("Dashboard", style = MaterialTheme.typography.titleLarge, color = OceanText)
        Text("Expenses by Category", style = MaterialTheme.typography.titleMedium, color = OceanText)
        for (item in data) {
            val pct = (item.total / total) * 100
            Text("${item.category}: £${"%.2f".format(item.total)} (${String.format("%.1f", pct)}%)", color = OceanText)
        }
    }
}
