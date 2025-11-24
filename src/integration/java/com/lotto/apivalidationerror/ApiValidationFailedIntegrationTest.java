package com.lotto.apivalidationerror;

import com.lotto.BaseIntegrationTest;
import com.lotto.infrastructure.apivalidation.ApiValidationErrorDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {


    private static final String INPUT_NUMBERS_ENDPOINT = "/inputNumbers";

    @Test
    @WithMockUser
    @DisplayName("Should return http status 400 and message when user provide empty numbers")
    void should_return_http_status_400_and_message_when_user_provide_empty_numbers() throws Exception {
        //given && when

        ApiValidationErrorDto apiValidationErrorDto = performAndExtractMethodsForApiValidation(INPUT_NUMBERS_ENDPOINT, """
                {
                "inputNumbers" : []
                }
                """.trim());

        //then
        assertAll(
                () -> assertThat(apiValidationErrorDto.messages())
                        .containsExactlyInAnyOrder("inputNumbers must not be empty"),
                () -> assertThat(apiValidationErrorDto).isNotNull(),
                () -> assertThat(apiValidationErrorDto.status()).isEqualTo(HttpStatus.BAD_REQUEST)
        );

    }

    @Test
    @WithMockUser
    @DisplayName("Should return http status 400 and message when user does not provide numbers")
    void should_return_http_status_400_and_message_when_user_does_not_provide_numbers() throws Exception {
        //given && when

        ApiValidationErrorDto mapped = performAndExtractMethodsForApiValidation(INPUT_NUMBERS_ENDPOINT, """
                {}
                """.trim());

        //then
        assertAll(
                ()-> assertThat(mapped.messages()).containsExactlyInAnyOrder("inputNumbers must not be empty",
                        "inputNumbers must not be null"),
                () -> assertThat(mapped.status()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(mapped).isNotNull()
        );


    }


    @Test
    @WithMockUser
    @DisplayName("Should return status bad request 400 and validation message when user provide empty password and invalid email")
    void should_return_status_bad_request_400_and_validation_message_when_user_provide_empty_password_and_invalid_email() throws Exception {

        //given && when

        ApiValidationErrorDto errors = performAndExtractMethods("/register", """
                {
                "username" : "email",
                "password" : ""
                }
                """.trim()
        );

        //then

        assertAll(
                () -> assertThat(errors.messages()).containsExactlyInAnyOrder(
                        "password must have min 6 length size",
                        "email must not be null", "email must not be empty",   "password must not be empty"),
                () -> assertThat(errors.status()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(errors).isNotNull()

        );


    }


    @Test
    @WithMockUser
    @DisplayName("Should return status bad request 400 and validation message when user provide any email and invalid password")
    void should_return_status_bad_request_400_and_validation_message_when_user_provide_any_email_and_invalid_password() throws Exception {

        //given && when

        ApiValidationErrorDto errors = performAndExtractMethods("/register", """
                {
                "password" : "1234"
                }
                """.trim()
        );
        //then

        assertAll(
                () -> assertThat(errors.messages()).containsExactlyInAnyOrder(
                        "password must have min 6 length size",
                        "email must not be empty", "email must not be null"),
                () -> assertThat(errors.status()).isEqualTo(HttpStatus.BAD_REQUEST),
                () -> assertThat(errors).isNotNull()

        );


    }


    private ApiValidationErrorDto performAndExtractMethods(String endpoint, String body) throws Exception {
        MvcResult result = mockMvc.perform(post(endpoint)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        return objectMapper.readValue(resultJson, ApiValidationErrorDto.class);
    }


    private ApiValidationErrorDto performAndExtractMethodsForApiValidation(String endpoint, String body) throws Exception {
        MvcResult result = mockMvc.perform(post(endpoint)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        return objectMapper.readValue(resultJson, ApiValidationErrorDto.class);
    }
}
