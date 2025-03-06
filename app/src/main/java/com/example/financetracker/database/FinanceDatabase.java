package com.example.financetracker.database;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.financetracker.model.Budget;
import com.example.financetracker.model.Expense;


@Database(entities = {Expense.class, Budget.class}, version = 3)
public abstract class FinanceDatabase extends RoomDatabase {
    private static FinanceDatabase instance;

    public abstract ExpenseDao expenseDao();

    public static synchronized FinanceDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            FinanceDatabase.class, "finance_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
