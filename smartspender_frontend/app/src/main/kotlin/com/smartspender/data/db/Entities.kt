package com.smartspender.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "income" or "expense"
    val amount: Double,
    val category: String,
    val date: String, // ISO yyyy-MM-dd
    val notes: String? = null
)

@Entity(tableName = "budget_goals")
data class BudgetGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val monthlyLimit: Double
)
