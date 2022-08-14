package me.stritzke.moneytracker.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/expenses")
@ExposesResourceFor(Expense.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ExpenseEndpoint implements RepresentationModelProcessor<RepositoryLinksResource> {
  private final ExpenseService expenseService;

  @RequestMapping(method = RequestMethod.GET)
  public RepresentationModel<Expense> getExpenseRoot() {
    RepresentationModel<Expense> expenseIndex = new RepresentationModel<>();
    expenseIndex.add(getLinkToExpenses("current", new DateWrapper()));
    return expenseIndex;
  }

  private Link getLinkToExpenses(String rel, DateWrapper date) {
    return WebMvcLinkBuilder.linkTo(methodOn(ExpenseEndpoint.class).getExpensesOfMonth(date.getYear(), date.getMonth())).withRel(rel);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<?> addExpense(@RequestBody ExpenseCreationDTO expenseCreationDTO) {
    DateWrapper date = new DateWrapper();
    Expense expense = expenseService.save(expenseCreationDTO.getAmount(), expenseCreationDTO.getComment(), date.getYear(), date.getMonth());
    URI location = WebMvcLinkBuilder.linkTo(methodOn(ExpenseEndpoint.class).getExpense(date.getYear(), date.getMonth(), expense.getNumericId())).toUri();
    return ResponseEntity.created(location).build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{year}/{month}")
  public ResponseEntity<CollectionModel<Expense>> getExpensesOfMonth(@PathVariable("year") Integer year, @PathVariable("month") Integer month) {
    Collection<Expense> byYearAndMonth = expenseService.find(new DateWrapper(year, month));
    byYearAndMonth.forEach(this::addSelfLink);
    CollectionModel<Expense> expenseResource = CollectionModel.of(byYearAndMonth);
    DateWrapper date = new DateWrapper(year, month);
    expenseResource.add(getLinkToExpenses("self", date));
    expenseResource.add(getLinkToExpenses("previous", date.getPreviousMonth()));
    expenseResource.add(getLinkToExpenses("next", date.getNextMonth()));
    return ResponseEntity.ok(expenseResource);
  }

  private void addSelfLink(Expense expense) {
    expense.add(linkTo(methodOn(ExpenseEndpoint.class)
        .getExpense(expense.getYear(), expense.getMonth(), expense.getNumericId()))
        .withSelfRel());
  }

  @RequestMapping(method = RequestMethod.POST, value = "/{year}/{month}")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> addExpenseInMonth(@PathVariable("year") Integer year,
                                             @PathVariable("month") Integer month,
                                             @RequestBody ExpenseCreationDTO expenseCreationDTO) throws URISyntaxException {
    Expense expense = expenseService.save(expenseCreationDTO.getAmount(), expenseCreationDTO.getComment(), year, month);
    URI location = WebMvcLinkBuilder.linkTo(methodOn(ExpenseEndpoint.class).getExpense(year, month, expense.getNumericId())).toUri();
    return ResponseEntity.created(location).build();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{year}/{month}/{expenseId}")
  public ResponseEntity<?> getExpense(@PathVariable("year") Integer year,
                                      @PathVariable("month") Integer month,
                                      @PathVariable("expenseId") Long expenseId) {
    Optional<Expense> expense = expenseService.findOne(expenseId);
    if (expense.isPresent()) {
      return ResponseEntity.ok(expense);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{year}/{month}/{expenseId}")
  public ResponseEntity<?> deleteExpense(@PathVariable("year") Integer year,
                                         @PathVariable("month") Integer month,
                                         @PathVariable("expenseId") Long expenseId) {
    expenseService.delete(expenseId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public RepositoryLinksResource process(RepositoryLinksResource resource) {
    resource.add(WebMvcLinkBuilder.linkTo(ExpenseEndpoint.class).withRel("expenses"));
    return resource;
  }
}
