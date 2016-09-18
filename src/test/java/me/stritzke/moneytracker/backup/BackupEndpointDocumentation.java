package me.stritzke.moneytracker.backup;

import me.stritzke.moneytracker.AbstractEndpointDocumentation;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BackupEndpointDocumentation extends AbstractEndpointDocumentation {
  private final String EXAMPLE_BACKUP = "{\"version\": 1,\"expenses\":[{\"amount\": 234, \"comment\": \"Something\", \"year\": 2016, \"month\": 9}, {\"amount\": 345, \"comment\": \"Another Thing\", \"year\": 2016, \"month\": 9}]}";

  @Test
  public void createBackup() throws Exception {
    createExpense(2016, 9);

    getMockMvc().perform(get("/api/backup").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("backup/create", responseFields(
                    fieldWithPath("version")
                            .type(JsonFieldType.NUMBER)
                            .description("Schema version"),
                    fieldWithPath("expenses")
                            .type(JsonFieldType.ARRAY)
                            .description("Array of all expenses stored in the application.")
            )));
  }

  @Test
  public void restoreBackup() throws Exception {
    getMockMvc().perform(post("/api/restore").content(EXAMPLE_BACKUP).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("backup/restore"));
  }

  @Test
  public void restoreBackupOverrideWarning() throws Exception {
    createExpense();

    getMockMvc().perform(post("/api/restore").content(EXAMPLE_BACKUP).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andDo(document("backup/restore_dataExisting"));
  }

  @Test
  public void restoreBackupForce() throws Exception {
    createExpense();

    getMockMvc().perform(post("/api/restore?force=true").content(EXAMPLE_BACKUP).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("backup/restore_force", requestParameters(
                    parameterWithName("force").description("Set to 'true', if the restore should be performed although the application is not empty.")
            )));
  }
}
