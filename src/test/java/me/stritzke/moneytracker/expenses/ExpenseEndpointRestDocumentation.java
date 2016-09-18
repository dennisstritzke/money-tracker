package me.stritzke.moneytracker.expenses;


import me.stritzke.moneytracker.AbstractEndpointDocumentation;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExpenseEndpointRestDocumentation extends AbstractEndpointDocumentation {
  private static final Snippet PATH_PARAMETER_YEAR_AND_MONTH = pathParameters(
      parameterWithName("year").description("The year the expense was made"),
      parameterWithName("month").description("The month of the year the expense was made in.")
  );

  @Test
  public void expensesRoot() throws Exception {
    getMockMvc().perform(get("/api/expenses").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("expenses/index", links(halLinks(),
            linkWithRel("current").description("Expenses of the current month")
        )));
  }

  @Test
  public void getExpenses() throws Exception {
    String location = createExpense(2016, 9);
    System.out.println(location);

    getMockMvc().perform(get("/api/expenses/{year}/{month}", 2016, 9).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("expenses/get", PATH_PARAMETER_YEAR_AND_MONTH, responseFields(
            fieldWithPath("_embedded.expenses[]._links.self")
                .type(JsonFieldType.OBJECT)
                .description("Link that identifies each expense"),
            fieldWithPath("_embedded.expenses[]")
                .type(JsonFieldType.ARRAY)
                .description("Array, which contains the expenses of the requested month"),
            fieldWithPath("_links").ignored(),
            fieldWithPath("_embedded").ignored()),
            links(halLinks(),
                linkWithRel("self").description("Expenses of the requested month"),
                linkWithRel("previous").description("Navigate to the expenses of the previous month"),
                linkWithRel("next").description("Navigate to the expenses of the next month")
            )));
  }

  @Test
  public void postExpense() throws Exception {
    getMockMvc().perform(post("/api/expenses/{year}/{month}", 2016, 7).content("{\"amount\":23.4,\"comment\":\"something\"}").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(document("expenses/creation", PATH_PARAMETER_YEAR_AND_MONTH,
            responseHeaders(headerWithName("Location").description("Location of the created expense"))
        ));
  }

  @Test
  public void postExpenseAtRoot() throws Exception {
    getMockMvc().perform(post("/api/expenses").content("{\"amount\":23.4,\"comment\":\"something\"}").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(document("expenses/creation_atRoot",
            responseHeaders(headerWithName("Location").description("Location of the created expense"))
        ));
  }

  @Test
  public void deleteExpense() throws Exception {
    String location = createExpense(2016, 9);

    getMockMvc().perform(delete(location))
        .andExpect(status().isNoContent());
  }
}
