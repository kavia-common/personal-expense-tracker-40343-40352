package com.smartspender.ui.add

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.smartspender.R
import com.smartspender.data.db.TransactionEntity
import com.smartspender.data.repo.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

// PUBLIC_INTERFACE
class AddTransactionActivity : Activity() {

    /** Simple Activity with classic views to add income/expense and persist via Room */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val typeField = findViewById<EditText>(R.id.typeField)
        val amountField = findViewById<EditText>(R.id.amountField)
        val categoryField = findViewById<EditText>(R.id.categoryField)
        val dateField = findViewById<EditText>(R.id.dateField)
        val notesField = findViewById<EditText>(R.id.notesField)
        val saveBtn = findViewById<Button>(R.id.saveBtn)

        // Default values
        typeField.setText("expense")
        val cal = Calendar.getInstance()
        val yyyy = cal.get(Calendar.YEAR)
        val mm = (cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val dd = cal.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        dateField.setText("$yyyy-$mm-$dd")

        dateField.setOnClickListener {
            val dp = DatePickerDialog(this, { _, y, m, d ->
                val month = (m + 1).toString().padStart(2, '0')
                val day = d.toString().padStart(2, '0')
                dateField.setText("$y-$month-$day")
            }, yyyy, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dp.show()
        }

        saveBtn.setOnClickListener {
            val type = if (typeField.text.toString().equals("income", true)) "income" else "expense"
            val amount = amountField.text.toString().toDoubleOrNull()
            val category = categoryField.text.toString().ifBlank { "General" }
            val date = dateField.text.toString()
            val notes = notesField.text.toString().ifBlank { null }

            if (amount == null) {
                Toast.makeText(this@AddTransactionActivity, "Enter valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repo = TransactionRepository(applicationContext)
            CoroutineScope(Dispatchers.IO).launch {
                repo.addTransaction(
                    TransactionEntity(
                        type = type,
                        amount = amount,
                        category = category,
                        date = date,
                        notes = notes
                    )
                )
                runOnUiThread {
                    Toast.makeText(this@AddTransactionActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
