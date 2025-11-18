package com.lotto.apivalidationerror;

import com.lotto.BaseIntegrationTest;
import com.lotto.infrastructure.apivalidation.ApiValidationErrorDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Should return http status 400 and message when user provide empty numbers")
    void should_return_http_status_400_and_message_when_user_provide_empty_numbers() throws Exception {
        //given && when
        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content(
                        """
                                {
                                "inputNumbers" : []
                                }
                                """.trim()
                ).contentType(MediaType.APPLICATION_JSON));

        MvcResult result = perform.andExpect(status().isBadRequest()).andReturn();
        String json = result.getResponse().getContentAsString();
        ApiValidationErrorDto apiValidationErrorDto = objectMapper.readValue(json, ApiValidationErrorDto.class);

        //then
        assertThat(apiValidationErrorDto.messages()).containsExactlyInAnyOrder("inputNumbers must not be empty");
    }

    @Test
    @DisplayName("Should return http status 400 and message when user does not provide numbers")
    void should_return_http_status_400_and_message_when_user_does_not_provide_numbers() throws Exception {
        //given && when
        ResultActions perform = mockMvc.perform(post("/inputNumbers").content(
                """
                        {}
                        """.trim()
        ).contentType(MediaType.APPLICATION_JSON));

        MvcResult result = perform.andExpect(status().isBadRequest()).andReturn();
        String json = result.getResponse().getContentAsString();
        ApiValidationErrorDto mapped = objectMapper.readValue(json, ApiValidationErrorDto.class);

        //then
        assertThat(mapped.messages()).containsExactlyInAnyOrder("inputNumbers must not be empty",
                "inputNumbers must not be null");
    }
}
