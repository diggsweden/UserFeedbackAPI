// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.endtoend;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import se.digg.api.dto.ApiKeyDTO;
import se.digg.api.form.RegisterForm;
import se.digg.api.model.Organisation;
import se.digg.api.service.ApiKeyService;
import se.digg.api.service.DomainService;
import se.digg.api.service.OrganisationService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(OrderAnnotation.class)
class RegisterOrganisationEndToEndTest {

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private WebTestClient testClient;

    private static final String ORGANISATION_NAME = "OrgTestABC123";
    private static final String SE_DOMAIN = "orgtestabc123.se";
    private static final String DE_DOMAIN = "orgtestabc123.de";

    private static final RegisterForm registerForm = new RegisterForm(ORGANISATION_NAME, Arrays.asList(SE_DOMAIN, DE_DOMAIN));
    private static final RegisterForm registerFormNoName = new RegisterForm("", Arrays.asList(SE_DOMAIN, DE_DOMAIN));
    private static final RegisterForm registerFormNoDomains = new RegisterForm(ORGANISATION_NAME, List.of());

    @Test
    @Order(1)
    void registerOrganisation() {

        // If a test failed, the organisation might already exist
        cleanUp();

        // When
        ResponseSpec response = testClient.post()
                .uri("/organisation/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerForm)
                .exchange();

        // Then
        ApiKeyDTO apiKeyDTO = response.expectStatus()
                .isOk()
                .expectBody(ApiKeyDTO.class)
                .returnResult()
                .getResponseBody();

        // Cleanup
        assertNotNull(apiKeyDTO);
        cleanUp();
    }

    @Test
    @Order(2)
    void registerOrganisationDuplicate() {

        // If a test failed, the organisation might already exist
        cleanUp();

        // When
        ResponseSpec response = testClient.post()
                .uri("/organisation/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerForm)
                .exchange();

        // Then
        ApiKeyDTO apiKeyDTO = response.expectStatus()
                .isOk()
                .expectBody(ApiKeyDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(apiKeyDTO);

        // When - Attempt duplicate registration
        ResponseSpec response2 = testClient.post()
                .uri("/organisation/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerForm)
                .exchange();

        // Then - Expect conflict
        response2.expectStatus()
                .is4xxClientError();

        // Cleanup
        cleanUp();
    }

    @Test
    @Order(3)
    void registerOrganisationWithoutAValidName() {

        // Given
        RegisterForm registerForm = new RegisterForm("", Arrays.asList(SE_DOMAIN, DE_DOMAIN));

        // When
        ResponseSpec response = testClient.post()
                .uri("/organisation/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerFormNoName)
                .exchange();

        // Then
        response.expectStatus().isBadRequest();
    }

    @Test
    @Order(4)
    void registerOrganisationWithoutADomain() {

        // When
        ResponseSpec response = testClient.post()
                .uri("/organisation/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerFormNoDomains)
                .exchange();

        // Then
        response.expectStatus().isBadRequest();
    }

    public void cleanUp() {
        Optional<Organisation> optionalOrganisation = organisationService.findByName(ORGANISATION_NAME);

        if (optionalOrganisation.isEmpty()) {
            return;
        }

        Long organisationId = optionalOrganisation.get().getId();

        // Cleanup organisation
        Long apiKeyId = apiKeyService.findByOrganisationId(organisationId).getId();
        domainService.findByOrganisationId(organisationId).forEach(domain -> domainService.delete(domain.getId()));
        apiKeyService.delete(apiKeyId);
        organisationService.delete(organisationId);
    }
}
