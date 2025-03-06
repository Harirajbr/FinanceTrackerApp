package com.example.financetracker.Classes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financetracker.R;
import com.example.financetracker.database.FinanceDatabase;
import com.example.financetracker.model.Expense;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText editAmount, editDate, editNotes;
    private Spinner spinnerCategory;
    private Button btnSaveExpense;
    private FinanceDatabase database;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private int expenseId = -1; // Default value for new expense

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        editAmount = findViewById(R.id.edit_amount);
        editDate = findViewById(R.id.edit_date);
        editNotes = findViewById(R.id.edit_notes);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

        database = FinanceDatabase.getInstance(this);

        // Populate spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        editDate.setOnClickListener(v -> showDatePickerDialog());

        // Check if this activity was opened for editing
        if (getIntent().hasExtra("expense_id")) {
            expenseId = getIntent().getIntExtra("expense_id", -1);
            loadExpenseData(expenseId);
        }

        btnSaveExpense.setOnClickListener(v -> saveExpense());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
            editDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void loadExpenseData(int expenseId) {
        executorService.execute(() -> {
            Expense expense = database.expenseDao().getExpenseById(expenseId);
            if (expense != null) {
                runOnUiThread(() -> {
                    editAmount.setText(String.valueOf(expense.getAmount()));
                    editDate.setText(expense.getDate());
                    editNotes.setText(expense.getNotes());

                    // Set the category in spinner
                    ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerCategory.getAdapter();
                    int position = adapter.getPosition(expense.getCategory());
                    spinnerCategory.setSelection(position);
                });
            }
        });
    }

    private void saveExpense() {
        String category = spinnerCategory.getSelectedItem().toString();
        String amountText = editAmount.getText().toString().trim();
        String date = editDate.getText().toString().trim();
        String notes = editNotes.getText().toString().trim();

        if (amountText.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Amount and Date are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountText);
        long timestamp = System.currentTimeMillis(); // Get current timestamp
        Expense expense = new Expense(category, amount, date, notes, timestamp);


        executorService.execute(() -> {
            if (expenseId == -1) {
                database.expenseDao().insertExpense(expense);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Expense Added!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                expense.setId(expenseId);
                database.expenseDao().updateExpense(expense);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Expense Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
}
