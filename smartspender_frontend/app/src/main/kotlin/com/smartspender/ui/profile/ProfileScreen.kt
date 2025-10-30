package com.smartspender.ui.profile

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
import com.smartspender.data.repo.TransactionRepository
import com.smartspender.ui.theme.OceanBackground
import com.smartspender.ui.theme.OceanText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = TransactionRepository(app)
    val goals = repo.goals().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@Composable
// PUBLIC_INTERFACE
fun ProfileScreen(vm: ProfileViewModel) {
    val goals by vm.goals.collectAsState()

    Surface(color = OceanBackground) {
        Text("Profile", style = MaterialTheme.typography.titleLarge, color = OceanText)
        Text("Name: Jane Doe", color = OceanText)
        Text("Email: jane.doe@example.com", color = OceanText)
        Divider()
        Text("Budget Goals", style = MaterialTheme.typography.titleMedium, color = OceanText)
        for (g in goals) {
            Text("${g.category}: £${"%.2f".format(g.monthlyLimit)} / month", color = OceanText)
        }
    }
}
