package me.stritzke.moneytracker.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ExpenseService {
  private final ExpenseRepository repository;

  Expense storeExpense(ExpenseCreationDTO expenseCreationDTO, Integer year, Integer month) {
    Expense expense = new Expense();
    expense.setAmount(expenseCreationDTO.getAmount());
    expense.setComment(expenseCreationDTO.getComment());
    expense.setYear(year);
    expense.setMonth(month);
    return repository.save(expense);
  }
}
