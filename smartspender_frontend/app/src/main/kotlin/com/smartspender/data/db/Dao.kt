package com.smartspender.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC")
    fun allTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tx: TransactionEntity)

    @Query("SELECT SUM(CASE WHEN type='income' THEN amount ELSE 0 END) FROM transactions")
    fun totalIncome(): Flow<Double?>

    @Query("SELECT SUM(CASE WHEN type='expense' THEN amount ELSE 0 END) FROM transactions")
    fun totalExpenses(): Flow<Double?>

    @Query("SELECT * FROM transactions WHERE (:type IS NULL OR type = :type) AND (:category IS NULL OR category = :category) ORDER BY date DESC, id DESC")
    fun filtered(type: String?, category: String?): Flow<List<TransactionEntity>>

    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE type='expense' GROUP BY category")
    fun expensesByCategory(): Flow<List<CategoryTotal>>
}

data class CategoryTotal(val category: String, val total: Double)

@Dao
interface BudgetGoalDao {
    @Query("SELECT * FROM budget_goals ORDER BY category ASC")
    fun goals(): Flow<List<BudgetGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: BudgetGoalEntity)
}
