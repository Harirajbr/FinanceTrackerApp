# Finance Tracker App

This is a simple Finance Tracker Android application that allows users to track their expenses, set budgets, and monitor spending.

## Features

- **Expense Tracking:**
  - Add new expenses with details like category, amount, date, and notes.
  - View a list of all expenses, with options to edit or delete them.
  - Filter expenses by category.
- **Budgeting:**
  - Set monthly budgets for different expense categories.
  - Monitor spending against the budget with a visual progress bar.
  - Receive alerts when spending exceeds the budget.
- **Data Visualization:**
  - (Potentially) Visualize spending patterns with graphs (MPAndroidChart library can be integrated).

## Installation

1. Clone the repository:
   ```bash
   git clone <https://github.com/Harirajbr/FinanceTrackerApp/>
   ```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## Dependencies

- AndroidX libraries (AppCompat, Material)
- Room Persistence Library
- Lifecycle ViewModel and LiveData
- RecyclerView
- (Potentially) MPAndroidChart

## Project Structure

```
app/src/main/java/com/example/financetracker/
├── Classes/                # Contains the main activities (MainActivity.java, AddExpenseActivity.java)
├── adapter/                # Contains the ExpenseAdapter.java for the RecyclerView
├── database/               # Contains the Room database setup (FinanceDatabase.java, ExpenseDao.java, BudgetDao.java)
├── model/                  # Contains the data models (Expense.java, Budget.java)
├── repository/             # Contains the ExpenseRepository.java for data access
├── viewmodel/              # Contains the ExpenseViewModel.java for managing UI-related data
├── res/layout/             # Contains the layout files for activities and list items
├── res/values/             # Contains resources like strings, colors, and styles
```

## Usage

1. Launch the app.
2. Add new expenses using the "+" button.
3. Set budgets for different categories.
4. Monitor your spending on the main screen.

