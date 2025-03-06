package com.example.financetracker.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.financetracker.model.Expense;
import com.example.financetracker.model.Budget;
import java.util.List;

@Dao
public interface ExpenseDao {

    // Insert, Update, and Delete operations for Expense
    @Insert
    void insertExpense(Expense expense);

    @Update
    void updateExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);

    // Get a single expense by ID
    @Query("SELECT * FROM expenses WHERE id = :expenseId LIMIT 1")
    Expense getExpenseById(int expenseId);

    // Get all expenses ordered by date (DESC)
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses();

    // Get all expenses of a specific category
    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesByCategory(String category);

    // Get total spending per category in the current month
    @Query("SELECT SUM(amount) FROM expenses WHERE category = :category")
    LiveData<Double> getTotalSpentForCategory(String category);

    // =================== Budget Queries ===================

    // Insert or update budget
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateBudget(Budget budget);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBudget(Budget budget);

    @Query("UPDATE budget_table SET totalSpent = totalSpent + :amount WHERE category = :category")
    void updateTotalSpent(String category, double amount);

    @Query("SELECT * FROM budget_table WHERE category = :category AND monthTimestamp = :monthTimestamp LIMIT 1")
    LiveData<Budget> getBudgetForCategory(String category, long monthTimestamp);



    // Get budget for a category (fix table name)
    @Query("SELECT monthlyLimit FROM budget_table WHERE category = :category LIMIT 1")
    LiveData<Double> getBudgetForCategory(String category);

    // Get all budgets (fix table name)
    @Query("SELECT * FROM budget_table ORDER BY category ASC")
    LiveData<List<Budget>> getAllBudgets();

}
