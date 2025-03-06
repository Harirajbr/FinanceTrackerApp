package com.example.financetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financetracker.R;
import com.example.financetracker.model.Expense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private final List<Expense> allExpenses;
    private List<Expense> filteredExpenses;
    private String selectedCategory = null;
    private final OnExpenseClickListener listener;
    private final Map<String, Double> budgetMap;

    public interface OnExpenseClickListener {
        void onEditExpense(Expense expense);
        void onDeleteExpense(Expense expense);
    }

    public ExpenseAdapter(List<Expense> expenseList, OnExpenseClickListener listener) {
        this.allExpenses = new ArrayList<>(expenseList);
        this.filteredExpenses = new ArrayList<>(expenseList);
        this.listener = listener;
        this.budgetMap = new HashMap<>();
        createBudget();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = filteredExpenses.get(position);
        holder.textCategory.setText(expense.getCategory());
        holder.textAmount.setText(String.format("₹%.2f", expense.getAmount()));
        holder.textDate.setText(expense.getDate());
        holder.textNotes.setText(expense.getNotes());

        double budgetLimit = budgetMap.getOrDefault(expense.getCategory(), 0.0);
        double totalSpent = getTotalSpentForCategory(expense.getCategory());
        int progress = (budgetLimit > 0) ? (int) ((totalSpent / budgetLimit) * 100) : 0;
        holder.budgetProgressBar.setProgress(Math.min(progress, 100));

        holder.imgEdit.setOnClickListener(v -> listener.onEditExpense(expense));
        holder.imgDelete.setOnClickListener(v -> listener.onDeleteExpense(expense));
    }

    @Override
    public int getItemCount() {
        return filteredExpenses.size();
    }

    public void updateData(List<Expense> newExpenses) {
        this.allExpenses.clear();
        this.allExpenses.addAll(newExpenses);
        filterByCategory(selectedCategory); // Ensure filtering happens after updating data
    }

    public void createBudget() {
        Map<String, Double> budgetLimits = new HashMap<>();
        budgetLimits.put("Food", 5000.0);
        budgetLimits.put("Transport", 3000.0);
        budgetLimits.put("Entertainment", 4000.0);
        budgetLimits.put("Shopping", 7000.0);
        budgetLimits.put("Health", 5000.0);
        budgetLimits.put("Utilities", 6000.0);
        budgetLimits.put("Others", 3000.0);

        setBudgets(budgetLimits);
    }



    private double getTotalSpentForCategory(String category) {
        double total = 0.0;
        for (Expense expense : allExpenses) {
            if (category == null || expense.getCategory().equals(category)) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    public void filterByCategory(String category) {
        selectedCategory = category;
        filteredExpenses.clear();

        if (category == null || category.equals("All Categories")) {
            filteredExpenses.addAll(allExpenses);
        } else {
            for (Expense expense : allExpenses) {
                if (expense.getCategory().equals(category)) {
                    filteredExpenses.add(expense);
                }
            }
        }

        notifyDataSetChanged();
    }


    public void setBudgets(Map<String, Double> budgets) {
        this.budgetMap.clear();
        this.budgetMap.putAll(budgets);
        notifyDataSetChanged(); // Refresh UI after setting budget
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView textCategory, textAmount, textDate, textNotes;
        ImageView imgEdit, imgDelete;
        ProgressBar budgetProgressBar;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.textCategory);
            textAmount = itemView.findViewById(R.id.textAmount);
            textDate = itemView.findViewById(R.id.textDate);
            textNotes = itemView.findViewById(R.id.textNotes);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            budgetProgressBar = itemView.findViewById(R.id.budgetProgressBar);
        }
    }
}

