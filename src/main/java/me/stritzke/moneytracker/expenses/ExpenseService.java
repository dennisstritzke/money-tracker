package me.stritzke.moneytracker.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpenseService {
  private final ExpenseRepository repository;

  Expense save(ExpenseCreationDTO expenseCreationDTO, Integer year, Integer month) {
    Expense expense = new Expense();
    expense.setAmount(expenseCreationDTO.getAmount());
    expense.setComment(expenseCreationDTO.getComment());
    expense.setYear(year);
    expense.setMonth(month);
    return repository.save(expense);
  }

  Expense findOne(long expenseId) {
    return repository.findOne(expenseId);
  }

  Collection<Expense> find(DateWrapper date) {
    return repository.findByYearAndMonth(date.getYear(), date.getMonth());
  }

  void delete(Long expenseId) {
    repository.delete(expenseId);
  }

  public Collection<Expense> findAll() {
    Iterable<Expense> expenseIterator = repository.findAll();
    Collection<Expense> expenses = new LinkedList<>();
    expenseIterator.forEach(expenses::add);
    return expenses;
  }
}
