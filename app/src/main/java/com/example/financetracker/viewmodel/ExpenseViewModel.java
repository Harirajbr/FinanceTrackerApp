package com.example.financetracker.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.financetracker.model.Expense;
import com.example.financetracker.model.Budget;
import com.example.financetracker.repository.ExpenseRepository;

import java.util.Calendar;
import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository repository;
    private LiveData<List<Expense>> allExpenses;
    private LiveData<List<Budget>> allBudgets;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();

    }

    // Expense operations
    public void insertExpense(Expense expense) {
        repository.insertExpense(expense);
    }

    public void updateExpense(Expense expense) {
        repository.updateExpense(expense);
    }

    public void deleteExpense(Expense expense) {
        repository.deleteExpense(expense);
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return repository.getExpensesByCategory(category);
    }



    // Budget operations
    public void insertOrUpdateBudget(Budget budget) {
        repository.insertOrUpdateBudget(budget);
    }


    public LiveData<List<Budget>> getAllBudgets() {
        return allBudgets;
    }

    public LiveData<Double> getBudgetForCategory(String category) {
        return repository.getBudgetForCategory(category);
    }


    // Get total spent for category in the current month
    public LiveData<Double> getTotalSpentForCategory(String category) {
        return repository.getTotalSpentForCategory(category);
    }
    private long getMonthTimestamp() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

}
