package me.stritzke.moneytracker.expenses;


import me.stritzke.moneytracker.Application;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ExpenseEndpointRestDocumentation {
  @Rule
  public RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");
  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
        .apply(documentationConfiguration(this.restDocumentation))
        .build();
  }

  @Test
  public void rootResourceTest() throws Exception {
    mockMvc.perform(get("/expenses").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("index"));
  }

  @Test
  public void postExpense() throws Exception {
    mockMvc.perform(post("/expenses/{year}/{month}", 2016, 7).content("{\"amount\":23.4,\"comment\":\"something\"}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(document("creation", pathParameters(
                    parameterWithName("year").description("The year the expense was made"),
                    parameterWithName("month").description("The month of the year the expense was made in.")
            )));
  }
}