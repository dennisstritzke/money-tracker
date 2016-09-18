package me.stritzke.moneytracker;

import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RootEndpointDocumentation extends AbstractEndpointDocumentation {
  @Test
  public void rootResource() throws Exception {
    getMockMvc().perform(get("/api").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("root", links(halLinks(),
            linkWithRel("doc").description("API documentation"),
            linkWithRel("expenses").description("Expenses resource"),
            linkWithRel("backup").description("Create a backup of all data stored the application"),
            linkWithRel("restore").description("Restore a previously created backup"),
            linkWithRel("profile").ignored()
        )));
  }
}
