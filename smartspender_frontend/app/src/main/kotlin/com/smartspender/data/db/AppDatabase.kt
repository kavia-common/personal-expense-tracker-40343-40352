package com.smartspender.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [TransactionEntity::class, BudgetGoalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetGoalDao(): BudgetGoalDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        // PUBLIC_INTERFACE
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smartspender.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        seed(context)
                    }
                }).build().also { INSTANCE = it }
            }

        private fun seed(context: Context) {
            val database = get(context)
            CoroutineScope(Dispatchers.IO).launch {
                val txDao = database.transactionDao()
                val goalDao = database.budgetGoalDao()

                // Sample goals
                goalDao.insert(BudgetGoalEntity(category = "Food", monthlyLimit = 300.0))
                goalDao.insert(BudgetGoalEntity(category = "Transport", monthlyLimit = 120.0))
                goalDao.insert(BudgetGoalEntity(category = "Entertainment", monthlyLimit = 150.0))

                // Sample transactions
                val today = "2025-01-10"
                txDao.insert(TransactionEntity(type = "income", amount = 2500.0, category = "Salary", date = today, notes = "Monthly salary"))
                txDao.insert(TransactionEntity(type = "expense", amount = 45.0, category = "Food", date = today, notes = "Groceries"))
                txDao.insert(TransactionEntity(type = "expense", amount = 15.0, category = "Transport", date = today, notes = "Bus pass"))
                txDao.insert(TransactionEntity(type = "expense", amount = 60.0, category = "Entertainment", date = today, notes = "Concert"))
                txDao.insert(TransactionEntity(type = "income", amount = 200.0, category = "Freelance", date = today, notes = "Design work"))
            }
        }
    }
}
