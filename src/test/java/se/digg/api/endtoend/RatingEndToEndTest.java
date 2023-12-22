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
import se.digg.api.config.RatingConfig;
import se.digg.api.dto.ApiKeyDTO;
import se.digg.api.dto.ImpressionDTO;
import se.digg.api.dto.RatingDTO;
import se.digg.api.form.ImpressionForm;
import se.digg.api.form.RatingForm;
import se.digg.api.form.RegisterForm;
import se.digg.api.model.Context;
import se.digg.api.model.Organisation;
import se.digg.api.service.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(OrderAnnotation.class)
class RatingEndToEndTest {

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
    private RatingService ratingService;

    @Autowired
    private RatingConfig ratingConfig;

    @Autowired
    private RatingConfig impressionConfig;

    @Autowired
    private WebTestClient testClient;

    private static final String ORGANISATION_NAME = "OrgTestABC789";
    private static final String SE_DOMAIN = "orgtestabc789.se";
    private static final String DE_DOMAIN = "orgtestabc789.de";
    private static final String FINGERPRINT = "fingerprintabc789-0";
    private static final String FINGERPRINT_2 = "fingerprintabc789-1";
    private static final String FINGERPRINT_3 = "fingerprintabc789-2";
    private static final String FINGERPRINT_4 = "fingerprintabc789-4";
    private static final String PATH = "/pathabc789";
    private static final String NAME = "nameabc789";
    private static final String HTML_ID = "htmlidabc789";
    private static final String TAG = "tagabc789";
    private static final String LABEL = "labelabc789";

    private static final String AUTH_HEADER_NAME = "X-API-KEY";

    private static final RegisterForm registerForm = new RegisterForm(ORGANISATION_NAME, Arrays.asList(SE_DOMAIN, DE_DOMAIN));
    private static final ImpressionForm impressionForm = new ImpressionForm(FINGERPRINT, SE_DOMAIN, PATH, NAME, HTML_ID, Arrays.asList(TAG), Arrays.asList(LABEL));
    private static final RatingForm ratingForm = new RatingForm(0L, FINGERPRINT, 5);

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
    @Order(10)
    void sendRating() {

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
            ratingForm.setImpressionId(impressionDTO.getId());
            ResponseSpec response2 = testClient.post()
                    .uri("/rating/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(ratingForm)
                    .exchange();

            // Then
            RatingDTO ratingDTO = response2.expectStatus()
                    .isOk()
                    .expectBody(RatingDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(ratingDTO);

            // Cleanup
            cleanUp();
        });
    }

    @Test
    @Order(11)
    void sendRatingDuplicate() {

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
            ratingForm.setImpressionId(impressionDTO.getId());
            ResponseSpec response2 = testClient.post()
                    .uri("/rating/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(ratingForm)
                    .exchange();

            // Then
            RatingDTO ratingDTO = response2.expectStatus()
                    .isOk()
                    .expectBody(RatingDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(ratingDTO);

            // When
            ResponseSpec response3 = testClient.post()
                    .uri("/rating/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(ratingForm)
                    .exchange();

            // Then - Expect idempotent response
            RatingDTO ratingDTODuplicate = response3.expectStatus()
                    .isOk()
                    .expectBody(RatingDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(ratingDTODuplicate);
            assertEquals(ratingDTO, ratingDTODuplicate);

            // Cleanup
            cleanUp();
        });
    }

    @Test
    @Order(12)
    void sendRatingsAndCalculateAverageAfterExpiryHasElapsedForASingleRating() {

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
            ratingForm.setImpressionId(impressionDTO.getId());
            ResponseSpec response2 = testClient.post()
                    .uri("/rating/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(ratingForm)
                    .exchange();

            // Then
            RatingDTO ratingDTO = response2.expectStatus()
                    .isOk()
                    .expectBody(RatingDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(ratingDTO);

            try {
                Thread.sleep(ratingConfig.getExpirationTimeInSeconds() * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Impression and Rating number 2
            // When
            impressionForm.setFingerprint(FINGERPRINT_2);
            ResponseSpec response3 = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionForm)
                    .exchange();

            // Then
            ImpressionDTO impressionDTO2 = response3.expectStatus()
                    .isOk()
                    .expectBody(ImpressionDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(impressionDTO2);

            // When
            ratingForm.setImpressionId(impressionDTO2.getId());
            ratingForm.setFingerprint(FINGERPRINT_2);
            ResponseSpec response4 = testClient.post()
                    .uri("/rating/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(ratingForm)
                    .exchange();

            // Then
            RatingDTO ratingDTO2 = response4.expectStatus()
                    .isOk()
                    .expectBody(RatingDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(ratingDTO2);

            // Impression and Rating number 3
            // When
            impressionForm.setFingerprint(FINGERPRINT_3);
            ResponseSpec response5 = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionForm)
                    .exchange();

            // Then
            ImpressionDTO impressionDTO3 = response5.expectStatus()
                    .isOk()
                    .expectBody(ImpressionDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(impressionDTO3);

            // When
            ratingForm.setImpressionId(impressionDTO3.getId());
            ratingForm.setFingerprint(FINGERPRINT_3);
            ratingForm.setScore(1);
            ResponseSpec response6 = testClient.post()
                    .uri("/rating/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(ratingForm)
                    .exchange();

            // Then
            RatingDTO ratingDTO3 = response6.expectStatus()
                    .isOk()
                    .expectBody(RatingDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(ratingDTO3);

            // Impression number 4
            // When
            impressionForm.setFingerprint(FINGERPRINT_4);
            ResponseSpec response7 = testClient.post()
                    .uri("/impression/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTH_HEADER_NAME, apiKey)
                    .bodyValue(impressionForm)
                    .exchange();

            // Then
            ImpressionDTO impressionDTO4 = response7.expectStatus()
                    .isOk()
                    .expectBody(ImpressionDTO.class)
                    .returnResult()
                    .getResponseBody();

            assertNotNull(impressionDTO4);

            // Assert that the average rating is 3.0 and that the number of ratings is 2 since one rating has expired
            assertEquals(impressionDTO4.getAvgRating(), new BigDecimal("3.0"));
            assertEquals(impressionDTO4.getRatingCount(), 2);

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
        List<Context> contexts = contextService.findByOrganisationId(organisationId);

        // Cleanup impression
        contexts.forEach(context -> impressionService.findByContextId(context.getId()).forEach(impression -> impressionService.delete(impression.getId())));

        // Cleanup rating
        contexts.forEach(context -> ratingService.findByContextId(context.getId()).forEach(rating -> ratingService.delete(rating.getId())));

        // Cleanup contextMeta and context
        contexts.forEach(context -> contextMetaService.findByContextId(context.getId()).forEach(contextMeta -> contextMetaService.delete(contextMeta.getId())));
        contexts.forEach(context -> contextService.delete(context.getId()));


        // Cleanup organisation
        Long apiKeyId = apiKeyService.findByOrganisationId(organisationId).getId();
        domainService.findByOrganisationId(organisationId).forEach(domain -> domainService.delete(domain.getId()));
        apiKeyService.delete(apiKeyId);
        organisationService.delete(organisationId);
    }
}
