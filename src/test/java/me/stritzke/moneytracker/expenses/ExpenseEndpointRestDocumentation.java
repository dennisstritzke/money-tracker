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
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ExpenseEndpointRestDocumentation {
  @Rule
  public RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");
  @Autowired
  private WebApplicationContext context;
  private static final Snippet PATH_PARAMETER_YEAR_AND_MONTH = pathParameters(
          parameterWithName("year").description("The year the expense was made"),
          parameterWithName("month").description("The month of the year the expense was made in.")
  );

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .apply(documentationConfiguration(this.restDocumentation))
            .build();
  }

  @Test
  public void expensesRoot() throws Exception {
    mockMvc.perform(get("/expenses").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("index", links(halLinks(),
                    linkWithRel("current").description("Expenses of the current month")
            )));
  }

  @Test
  public void getExpenses() throws Exception {
    mockMvc.perform(get("/expenses/{year}/{month}", 2099, 1).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("getExpenses", PATH_PARAMETER_YEAR_AND_MONTH, links(halLinks(),
                    linkWithRel("self").description("Expenses of the current month"),
                    linkWithRel("previous").description("Expenses of the previous month"),
                    linkWithRel("next").description("Expenses of the next month")
            )));
  }

  @Test
  public void postExpense() throws Exception {
    mockMvc.perform(post("/expenses/{year}/{month}", 2016, 7).content("{\"amount\":23.4,\"comment\":\"something\"}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(document("creation", PATH_PARAMETER_YEAR_AND_MONTH,
                    responseHeaders(headerWithName("Location").description("Location of the created expense"))
            ));
  }
}
