package me.stritzke.moneytracker;

import lombok.Getter;
import me.stritzke.moneytracker.expenses.ExpenseService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public abstract class AbstractEndpointDocumentation {
  @Rule
  public RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");
  @Autowired
  private WebApplicationContext context;
  @Autowired
  private ExpenseService expenseService;

  @Getter
  private MockMvc mockMvc;

  @Before
  public void setUp() {
    expenseService.findAll().forEach(expense -> {
      expenseService.delete(expense.getNumericId());
    });

    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .apply(documentationConfiguration(this.restDocumentation))
            .build();
  }

  protected String createExpense() throws Exception {
    return createExpense(0, 0);
  }

  protected String createExpense(Integer year, Integer month) throws Exception {
    final String[] location = new String[1];
    getMockMvc().perform(post("/api/expenses/{year}/{month}", year, month).content("{\"amount\":23.4,\"comment\":\"something\"}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(result -> location[0] = result.getResponse().getHeader("Location"));
    return location[0];
  }
}
