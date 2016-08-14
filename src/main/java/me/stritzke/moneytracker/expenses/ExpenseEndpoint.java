package me.stritzke.moneytracker.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpenseEndpoint {
  private final ExpenseRepository repository;

  @RequestMapping(method = RequestMethod.GET)
  public Link getExpenseRoot() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);

    return ControllerLinkBuilder.linkTo(methodOn(ExpenseEndpoint.class).getExpensesOfMonth(year, month)).withRel("current");
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{year}/{month}")
  public ResponseEntity<List<Expense>> getExpensesOfMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month) {
    return ResponseEntity.ok(repository.findByYearAndMonth(year, month));
  }

  @RequestMapping(method = RequestMethod.POST, value = "/{year}/{month}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> addExpense(@PathVariable("year") Integer year,
                         @PathVariable("month") Integer month,
                         @RequestBody ExpenseCreationDTO expenseCreationDTO) throws URISyntaxException {
    Expense expense = new Expense();
    expense.setAmount(expenseCreationDTO.getAmount());
    expense.setComment(expenseCreationDTO.getComment());
    expense.setYear(year);
    expense.setMonth(month);
    expense = repository.save(expense);
    URI location = ControllerLinkBuilder.linkTo(methodOn(ExpenseEndpoint.class).getExpense(year, month, expense.getId())).toUri();
    return ResponseEntity.created(location).build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{year}/{month}/{expenseId}")
  public ResponseEntity<?> getExpense(@PathVariable("year") Integer year,
                                            @PathVariable("month") Integer month,
                                            @PathVariable("expenseId") Long expenseId) {
    Expense expense = repository.findOne(expenseId);
    if(expense == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(expense);
    }
  }
}
