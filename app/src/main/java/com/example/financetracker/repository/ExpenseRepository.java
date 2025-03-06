package com.example.financetracker.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.financetracker.database.FinanceDatabase;
import com.example.financetracker.database.ExpenseDao;
import com.example.financetracker.model.Budget;
import com.example.financetracker.model.Expense;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseRepository {
    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ExpenseRepository(Application application) {
        FinanceDatabase database = FinanceDatabase.getInstance(application);
        expenseDao = database.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
    }

    public void insertExpense(Expense expense) {
        executorService.execute(() -> expenseDao.insertExpense(expense));
    }

    public void updateExpense(Expense expense) {
        executorService.execute(() -> expenseDao.updateExpense(expense));
    }

    public void deleteExpense(Expense expense) {
        executorService.execute(() -> expenseDao.deleteExpense(expense));
    }

    public void insertOrUpdateBudget(Budget budget) {
        executorService.execute(() -> expenseDao.insertOrUpdateBudget(budget));
    }

    public LiveData<Double> getTotalSpentForCategory(String category) {
        return expenseDao.getTotalSpentForCategory(category);
    }

    public LiveData<Double> getBudgetForCategory(String category) {
        return expenseDao.getBudgetForCategory(category);
    }




    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }
    public LiveData<List<Expense>> getExpensesByCategory(String category) {
        return expenseDao.getExpensesByCategory(category);
    }

}

