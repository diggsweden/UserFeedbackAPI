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
import se.digg.api.config.ImpressionConfig;
import se.digg.api.dto.ApiKeyDTO;
import se.digg.api.dto.ImpressionDTO;
import se.digg.api.form.ImpressionForm;
import se.digg.api.form.RegisterForm;
import se.digg.api.model.Context;
import se.digg.api.model.Organisation;
import se.digg.api.service.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(OrderAnnotation.class)
class ImpressionEndToEndTest {

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private ImpressionService impressionService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private ContextMetaService contextMetaService;

    @Autowired
    private ImpressionConfig impressionConfig;

    @Autowired
    private WebTestClient testClient;

    private static final String ORGANISATION_NAME = "OrgTestABC345";
    private static final String SE_DOMAIN = "orgtestabc345.se";
    private static final String DE_DOMAIN = "orgtestabc345.de";
    private static final String FINGERPRINT = "fingerprintabc345";
    private static final String PATH = "/pathabc345";
    private static final String NAME = "nameabc345";
    private static final String HTML_ID = "htmlidabc345";
    private static final String TAG = "tagabc345";
    private static final String LABEL = "labelabc345";

    private static final String AUTH_HEADER_NAME = "X-API-KEY";

    private static final RegisterForm registerForm = new RegisterForm(ORGANISATION_NAME, Arrays.asList(SE_DOMAIN, DE_DOMAIN));

    // @formatter:off
    private static final ImpressionForm impressionForm =                    new ImpressionForm(FINGERPRINT, SE_DOMAIN, PATH, NAME, HTML_ID, Arrays.asList(TAG), Arrays.asList(LABEL));
    private static final ImpressionForm impressionFormNoFingerprint =       new ImpressionForm("", SE_DOMAIN, PATH, NAME, HTML_ID, Arrays.asList(TAG), Arrays.asList(LABEL));
    private static final ImpressionForm impressionFormNoDomain =            new ImpressionForm(FINGERPRINT, "", PATH, NAME, HTML_ID, Arrays.asList(TAG), Arrays.asList(LABEL));
    private static final ImpressionForm impressionFormNoPath =              new ImpressionForm(FINGERPRINT, SE_DOMAIN, "", NAME, HTML_ID, Arrays.asList(TAG), Arrays.asList(LABEL));
    private static final ImpressionForm impressionFormNoName =              new ImpressionForm(FINGERPRINT, SE_DOMAIN, PATH, "", HTML_ID, Arrays.asList(TAG), Arrays.asList(LABEL));
    private static final ImpressionForm impressionFormOnlyRequiredFields =  new ImpressionForm(FINGERPRINT, SE_DOMAIN, PATH, NAME, "", Arrays.asList(), Arrays.asList());
    // @formatter:on

    private void registerOrganisation() {

        // If a test failed, the organisation might already exist
        cleanUp();

        testClient.post()
                .uri("/organisation/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerForm)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ApiKeyDTO.class);
    }

    @Test
    @Order(5)
    void sendImpression() {

        // Given
        registerOrganisation();
        Optional<Organisation> optionalOrganisation = organisationService.findByName(ORGANISATION_NAME);

        optionalOrganisation.ifPresent(organisation -> {
            Long organisationId = organisation.getId();

            String apiKey = apiKeyService.findByOrganisationId(organisationId).getUuid4();
            assertNotNull(apiKey);

            // When
            ResponseSpec response = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionForm)
                    .exchange();

            // Then
            ImpressionDTO impressionDTO = response.expectStatus()
                    .isOk()
                    .expectBody(ImpressionDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(impressionDTO);

            // Cleanup
            cleanUp();
        });
    }

    @Test
    @Order(6)
    void sendImpressionDuplicate() {

        // Given
        registerOrganisation();

        Optional<Organisation> optionalOrganisation = organisationService.findByName(ORGANISATION_NAME);
        optionalOrganisation.ifPresent(organisation -> {
            Long organisationId = organisation.getId();

            String apiKey = apiKeyService.findByOrganisationId(organisationId).getUuid4();
            assertNotNull(apiKey);

            // When
            ResponseSpec response = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionForm)
                    .exchange();

            // Then
            ImpressionDTO impressionDTO = response.expectStatus()
                    .isOk()
                    .expectBody(ImpressionDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(impressionDTO);

            // When
            ResponseSpec response2 = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionForm)
                    .exchange();

            // Then - Expect idempotent response
            ImpressionDTO impressionDTODuplicate = response2.expectStatus()
                    .isOk()
                    .expectBody(ImpressionDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(impressionDTODuplicate);
            assertEquals(impressionDTO, impressionDTODuplicate);

            // Cleanup
            cleanUp();
        });
    }

    @Test
    @Order(7)
    void sendIncompleteImpressions() {

        // Given
        registerOrganisation();

        Optional<Organisation> optionalOrganisation = organisationService.findByName(ORGANISATION_NAME);
        optionalOrganisation.ifPresent(organisation -> {
            Long organisationId = organisation.getId();

            String apiKey = apiKeyService.findByOrganisationId(organisationId).getUuid4();
            assertNotNull(apiKey);

            // When
            ResponseSpec response = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionFormNoFingerprint)
                    .exchange();

            // Then
            response.expectStatus().isBadRequest();

            // When
            ResponseSpec response2 = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionFormNoDomain)
                    .exchange();

            // Then
            response2.expectStatus().isBadRequest();

            // When
            ResponseSpec response3 = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionFormNoPath)
                    .exchange();

            // Then
            response3.expectStatus().isBadRequest();

            // When
            ResponseSpec response4 = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionFormNoName)
                    .exchange();

            // Then
            response4.expectStatus().isBadRequest();

            // Cleanup
            cleanUp();
        });
    }

    @Test
    @Order(8)
    void sendImpressionOnlyRequiredFields() {

        // Given
        registerOrganisation();

        Optional<Organisation> optionalOrganisation = organisationService.findByName(ORGANISATION_NAME);
        optionalOrganisation.ifPresent(organisation -> {
            Long organisationId = organisation.getId();

            String apiKey = apiKeyService.findByOrganisationId(organisationId).getUuid4();
            assertNotNull(apiKey);

            // When
            ResponseSpec response = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionFormOnlyRequiredFields)
                    .exchange();

            // Then
            ImpressionDTO impressionDTO = response.expectStatus()
                    .isOk()
                    .expectBody(ImpressionDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(impressionDTO);

            // Cleanup
            cleanUp();
        });
    }

    @Test
    @Order(9)
    void sendImpressionDuplicateAfterExpiryHasElapsed() {

        // Given
        registerOrganisation();

        Optional<Organisation> optionalOrganisation = organisationService.findByName(ORGANISATION_NAME);
        optionalOrganisation.ifPresent(organisation -> {
            Long organisationId = organisation.getId();

            String apiKey = apiKeyService.findByOrganisationId(organisationId).getUuid4();
            assertNotNull(apiKey);

            // When
            ResponseSpec response = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionForm)
                    .exchange();

            // Then
            ImpressionDTO impressionDTO = response.expectStatus()
                    .isOk()
                    .expectBody(ImpressionDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(impressionDTO);

            try {
                Thread.sleep(impressionConfig.getExpirationTimeInSeconds() * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // When
            ResponseSpec response2 = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionForm)
                    .exchange();

            // Then - After impression expiry time has elapsed expect a new impression to have been created
            ImpressionDTO impressionDTODuplicate = response2.expectStatus()
                    .isOk()
                    .expectBody(ImpressionDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(impressionDTODuplicate);
            assertNotEquals(impressionDTO, impressionDTODuplicate);

            // Cleanup
            cleanUp();
        });
    }

    public void cleanUp() {
        Optional<Organisation> optionalOrganisation = organisationService.findByName(ORGANISATION_NAME);

        if (optionalOrganisation.isEmpty()) {
            return;
        }

        Long organisationId = optionalOrganisation.get().getId();

        // Cleanup impression
        List<Context> contexts = contextService.findByOrganisationId(organisationId);
        contexts.forEach(context -> contextMetaService.findByContextId(context.getId()).forEach(contextMeta -> contextMetaService.delete(contextMeta.getId())));
        contexts.forEach(context -> impressionService.findByContextId(context.getId()).forEach(impression -> impressionService.delete(impression.getId())));
        contexts.forEach(context -> contextService.delete(context.getId()));


        // Cleanup organisation
        Long apiKeyId = apiKeyService.findByOrganisationId(organisationId).getId();
        domainService.findByOrganisationId(organisationId).forEach(domain -> domainService.delete(domain.getId()));
        apiKeyService.delete(apiKeyId);
        organisationService.delete(organisationId);
    }
}
