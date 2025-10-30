package com.smartspender.data.repo

import android.content.Context
import com.smartspender.data.db.AppDatabase
import com.smartspender.data.db.BudgetGoalEntity
import com.smartspender.data.db.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TransactionRepository(context: Context) {
    private val db = AppDatabase.get(context)
    private val txDao = db.transactionDao()
    private val goalDao = db.budgetGoalDao()

    // PUBLIC_INTERFACE
    fun allTransactions() = txDao.allTransactions()

    // PUBLIC_INTERFACE
    suspend fun addTransaction(entity: TransactionEntity) = txDao.insert(entity)

    // PUBLIC_INTERFACE
    fun totals(): Flow<Totals> = combine(
        txDao.totalIncome().map { it ?: 0.0 },
        txDao.totalExpenses().map { it ?: 0.0 }
    ) { income, expense ->
        Totals(income = income, expenses = expense, balance = income - expense)
    }

    // PUBLIC_INTERFACE
    fun filter(type: String?, category: String?) = txDao.filtered(type, category)

    // PUBLIC_INTERFACE
    fun goals(): Flow<List<BudgetGoalEntity>> = goalDao.goals()
}

data class Totals(val income: Double, val expenses: Double, val balance: Double)
