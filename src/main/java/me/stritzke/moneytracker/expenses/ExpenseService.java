package me.stritzke.moneytracker.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpenseService {
  private final ExpenseRepository repository;

  public Expense save(BigDecimal amount, String comment, Integer year, Integer month) {
    Expense expense = new Expense();
    expense.setAmount(amount);
    expense.setComment(comment);
    expense.setYear(year);
    expense.setMonth(month);
    return repository.save(expense);
  }

  Optional<Expense> findOne(long expenseId) {
    return repository.findById(expenseId);
  }

  Collection<Expense> find(DateWrapper date) {
    return repository.findByYearAndMonth(date.getYear(), date.getMonth());
  }

  public void delete(Long expenseId) {
    Optional<Expense> expense = repository.findById(expenseId);
    expense.ifPresent(repository::delete);
  }

  public Collection<Expense> findAll() {
    Iterable<Expense> expenseIterator = repository.findAll();
    Collection<Expense> expenses = new LinkedList<>();
    expenseIterator.forEach(expenses::add);
    return expenses;
  }
}
