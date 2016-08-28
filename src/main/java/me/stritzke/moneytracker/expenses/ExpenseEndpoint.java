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
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/expenses")
@ExposesResourceFor(Expense.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpenseEndpoint implements ResourceProcessor<RepositoryLinksResource> {
  private final ExpenseRepository repository;
  private final ExpenseService expenseService;

  @RequestMapping(method = RequestMethod.GET)
  public ResourceSupport getExpenseRoot() {
    Link currentLink = createCurrentLink();
    ResourceSupport expenseIndex = new ResourceSupport();
    expenseIndex.add(currentLink);
    return expenseIndex;
  }

  private Link createCurrentLink() {
    return getLinkToExpenses("current", new DateWrapper());
  }

  private Link getLinkToExpenses(String rel, DateWrapper date) {
    return ControllerLinkBuilder.linkTo(methodOn(ExpenseEndpoint.class).getExpensesOfMonth(date.getYear(), date.getMonth())).withRel(rel);
  }

  private Link getLinkToPreviousMonthExpenses(DateWrapper date) {
    return getLinkToExpenses("previous", date.getPreviousMonth());
  }

  private Link getLinkToNextMonthExpenses(DateWrapper date) {
    return getLinkToExpenses("next", date.getNextMonth());
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> addExpense(@RequestBody ExpenseCreationDTO expenseCreationDTO) {
    DateWrapper date = new DateWrapper();
    Expense expense = expenseService.storeExpense(expenseCreationDTO, date.getYear(), date.getMonth());
    URI location = ControllerLinkBuilder.linkTo(methodOn(ExpenseEndpoint.class).getExpense(date.getYear(), date.getMonth(), expense.getId())).toUri();
    return ResponseEntity.created(location).build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{year}/{month}")
  public ResponseEntity<Resources<Expense>> getExpensesOfMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month) {
    List<Expense> byYearAndMonth = repository.findByYearAndMonth(year, month);
    Resources<Expense> expenseResource = new Resources<Expense>(byYearAndMonth);
    DateWrapper date = new DateWrapper(year, month);
    expenseResource.add(getLinkToExpenses("self", date));
    expenseResource.add(getLinkToPreviousMonthExpenses(date));
    expenseResource.add(getLinkToNextMonthExpenses(date));
    return ResponseEntity.ok(expenseResource);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/{year}/{month}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> addExpenseInMonth(@PathVariable("year") Integer year,
                                             @PathVariable("month") Integer month,
                                             @RequestBody ExpenseCreationDTO expenseCreationDTO) throws URISyntaxException {
    Expense expense = expenseService.storeExpense(expenseCreationDTO, year, month);
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
