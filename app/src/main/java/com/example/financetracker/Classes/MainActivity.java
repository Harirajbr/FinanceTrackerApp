package com.example.financetracker.Classes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financetracker.R;
import com.example.financetracker.adapter.ExpenseAdapter;
import com.example.financetracker.model.Expense;
import com.example.financetracker.viewmodel.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends ComponentActivity {
    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter expenseAdapter;
    private Spinner spinnerCategory;
    private TextView txtTotalSpent, txtBudget;
    private List<String> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinnerCategory = findViewById(R.id.spinnerCategory);
        txtTotalSpent = findViewById(R.id.txtTotalSpent);
        txtBudget = findViewById(R.id.txtBudget);
        FloatingActionButton btnAddExpense = findViewById(R.id.btnAddExpense);

        btnAddExpense.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddExpenseActivity.class)));

        expenseAdapter = new ExpenseAdapter(new ArrayList<>(), new ExpenseAdapter.OnExpenseClickListener() {
            @Override
            public void onEditExpense(Expense expense) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                intent.putExtra("expense_id", expense.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteExpense(Expense expense) {
                expenseViewModel.deleteExpense(expense);
            }
        });

        recyclerView.setAdapter(expenseAdapter);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, this::updateCategoryFilter);
    }

    private void updateCategoryFilter(List<Expense> expenses) {
        expenseAdapter.updateData(expenses);

        Set<String> uniqueCategories = new HashSet<>();
        double totalSpentAllCategories = 0.0; // Track total spent for all categories

        for (Expense expense : expenses) {
            uniqueCategories.add(expense.getCategory());
            totalSpentAllCategories += expense.getAmount();
        }

        categoryList = new ArrayList<>(uniqueCategories);
        categoryList.add(0, "All Categories");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        double finalTotalSpentAllCategories = totalSpentAllCategories; // Capture in final for lambda
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categoryList.get(position);
                if (selectedCategory.equals("All Categories")) {
                    expenseAdapter.filterByCategory(null);
                    txtTotalSpent.setText(String.format(Locale.getDefault(), "Total Spent: $%.2f", finalTotalSpentAllCategories));
                    txtBudget.setText("Budget: --");
                } else {
                    expenseAdapter.filterByCategory(selectedCategory);
                    loadBudgetData(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                expenseAdapter.filterByCategory(null);
                txtTotalSpent.setText(String.format(Locale.getDefault(), "Total Spent: $%.2f", finalTotalSpentAllCategories));
                txtBudget.setText("Budget: --");
            }
        });
    }


    private void loadBudgetData(String category) {
        expenseViewModel.getTotalSpentForCategory(category).observe(this, totalSpentValue -> {
            final double totalSpent = (totalSpentValue == null) ? 0.0 : totalSpentValue;
            txtTotalSpent.setText(String.format(Locale.getDefault(), "Total Spent: $%.2f", totalSpent));

            expenseViewModel.getBudgetForCategory(category).observe(this, budgetValue -> {
                final double budget = (budgetValue == null) ? 0.0 : budgetValue;
                txtBudget.setText(String.format(Locale.getDefault(), "Budget: $%.2f", budget));

                if (totalSpent > budget && budget > 0) {
                    Toast.makeText(MainActivity.this, "Warning: You've exceeded your budget!", Toast.LENGTH_LONG).show();
                }
            });
        });
    }



    private long getStartOfMonthTimestamp() {
        return System.currentTimeMillis();
    }
}
