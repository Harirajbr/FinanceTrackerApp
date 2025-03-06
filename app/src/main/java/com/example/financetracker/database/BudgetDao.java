package com.example.financetracker.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financetracker.model.Budget;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertBudget(Budget budget);

    @Query("SELECT * FROM budget_table WHERE category = :category AND monthTimestamp = :month LIMIT 1")
    Budget getBudgetForCategory(String category, long month);

    @Update
    void updateBudget(Budget budget);
}

