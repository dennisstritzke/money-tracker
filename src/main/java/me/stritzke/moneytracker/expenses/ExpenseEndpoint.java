package me.stritzke.moneytracker.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.*;
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
@RequestMapping("/api/expenses")
@ExposesResourceFor(Expense.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpenseEndpoint implements ResourceProcessor<RepositoryLinksResource> {
  private final ExpenseRepository repository;

  @RequestMapping(method = RequestMethod.GET)
  public ResourceSupport getExpenseRoot() {
    Link currentLink = createCurrentLink();
    ResourceSupport expenseIndex = new ResourceSupport();
    expenseIndex.add(currentLink);
    return expenseIndex;
  }

  private Link createCurrentLink() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    return getLinkToExpenses("current", year, month);
  }

  private Link getLinkToExpenses(String rel, int year, int month) {
    return ControllerLinkBuilder.linkTo(methodOn(ExpenseEndpoint.class).getExpensesOfMonth(year, month)).withRel(rel);
  }

  private Link getLinkToPreviousMonthExpenses(int currentYear, int currentMonth) {
    if (currentMonth == 1) {
      return getLinkToExpenses("previous", currentYear - 1, 12);
    } else {
      return getLinkToExpenses("previous", currentYear, currentMonth - 1);
    }
  }

  private Link getLinkToNextMonthExpenses(int currentYear, int currentMonth) {
    if (currentMonth == 12) {
      return getLinkToExpenses("next", currentYear + 1, 1);
    } else {
      return getLinkToExpenses("next", currentYear, currentMonth + 1);
    }
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{year}/{month}")
  public ResponseEntity<Resources<Expense>> getExpensesOfMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month) {
    List<Expense> byYearAndMonth = repository.findByYearAndMonth(year, month);
    Resources<Expense> expenseResource = new Resources<Expense>(byYearAndMonth);
    expenseResource.add(getLinkToExpenses("self", year, month));
    expenseResource.add(getLinkToPreviousMonthExpenses(year, month));
    expenseResource.add(getLinkToNextMonthExpenses(year, month));
    return ResponseEntity.ok(expenseResource);
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
    if (expense == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(expense);
    }
  }

  @Override
  public RepositoryLinksResource process(RepositoryLinksResource resource) {
    resource.add(ControllerLinkBuilder.linkTo(ExpenseEndpoint.class).withRel("expenses"));
    return resource;
  }
}
