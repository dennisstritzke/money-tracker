package me.stritzke.moneytracker.expenses;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface ExpenseRepository extends CrudRepository<Expense, Long> {
  List<Expense> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
