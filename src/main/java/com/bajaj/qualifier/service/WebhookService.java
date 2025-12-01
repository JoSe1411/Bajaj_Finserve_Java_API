package com.bajaj.qualifier.service;

import com.bajaj.qualifier.dto.SqlSubmissionRequest;
import com.bajaj.qualifier.dto.WebhookRequest;
import com.bajaj.qualifier.dto.WebhookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Service class for handling webhook operations.
 * - Generates webhook by calling the API
 * - Submits SQL query to the webhook
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebClient webClient;

    @Value("${app.api.generate-webhook}")
    private String generateWebhookUrl;

    @Value("${app.user.name}")
    private String userName;

    @Value("${app.user.regNo}")
    private String userRegNo;

    @Value("${app.user.email}")
    private String userEmail;

    /**
     * SQL Query for Question 2:
     * For every department, calculate the average age of individuals with salaries
     * exceeding ₹70,000, and produce a concatenated string containing at most 10 names.
     * Ordered by DEPARTMENT_ID DESC.
     */
    private static final String FINAL_SQL_QUERY = 
        "SELECT d.DEPARTMENT_NAME, " +
        "AVG(TIMESTAMPDIFF(YEAR, e.DOB, CURDATE())) AS AVERAGE_AGE, " +
        "SUBSTRING_INDEX(GROUP_CONCAT(CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) " +
        "ORDER BY e.EMP_ID SEPARATOR ', '), ', ', 10) AS EMPLOYEE_LIST " +
        "FROM EMPLOYEE e " +
        "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
        "JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
        "WHERE p.AMOUNT > 70000 " +
        "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME " +
        "ORDER BY d.DEPARTMENT_ID DESC";

    /**
     * Generates webhook by sending POST request with user details.
     * @return WebhookResponse containing webhook URL and access token
     */
    public WebhookResponse generateWebhook() {
        log.info("=".repeat(60));
        log.info("STARTING: Generate Webhook Request");
        log.info("=".repeat(60));

        WebhookRequest request = WebhookRequest.builder()
                .name(userName)
                .regNo(userRegNo)
                .email(userEmail)
                .build();

        log.info("Request Details:");
        log.info("  - Name: {}", userName);
        log.info("  - RegNo: {}", userRegNo);
        log.info("  - Email: {}", userEmail);
        log.info("  - URL: {}", generateWebhookUrl);

        try {
            WebhookResponse response = webClient.post()
                    .uri(generateWebhookUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(WebhookResponse.class)
                    .block();

            if (response != null) {
                log.info("SUCCESS: Webhook generated successfully!");
                log.info("  - Webhook URL: {}", response.getWebhook());
                log.info("  - Access Token: {}...", 
                    response.getAccessToken() != null && response.getAccessToken().length() > 50 
                        ? response.getAccessToken().substring(0, 50) 
                        : response.getAccessToken());
            }

            return response;
        } catch (WebClientResponseException e) {
            log.error("ERROR: Failed to generate webhook");
            log.error("  - Status: {}", e.getStatusCode());
            log.error("  - Response: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Failed to generate webhook: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("ERROR: Unexpected error during webhook generation: {}", e.getMessage());
            throw new RuntimeException("Failed to generate webhook: " + e.getMessage(), e);
        }
    }

    /**
     * Submits the SQL query solution to the webhook URL.
     * @param webhookUrl The URL to submit the solution
     * @param accessToken JWT token for authorization
     */
    public void submitSolution(String webhookUrl, String accessToken) {
        log.info("=".repeat(60));
        log.info("STARTING: Submit SQL Solution");
        log.info("=".repeat(60));

        // Determine question based on regNo (Even = Question 2)
        int lastTwoDigits = getLastTwoDigits(userRegNo);
        String questionType = (lastTwoDigits % 2 == 0) ? "Question 2 (Even)" : "Question 1 (Odd)";
        
        log.info("RegNo Analysis:");
        log.info("  - RegNo: {}", userRegNo);
        log.info("  - Last 2 Digits: {}", lastTwoDigits);
        log.info("  - Assigned: {}", questionType);

        SqlSubmissionRequest request = SqlSubmissionRequest.builder()
                .finalQuery(FINAL_SQL_QUERY)
                .build();

        log.info("SQL Query to Submit:");
        log.info("  {}", FINAL_SQL_QUERY);
        log.info("Webhook URL: {}", webhookUrl);

        try {
            String response = webClient.post()
                    .uri(webhookUrl)
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("=".repeat(60));
            log.info("SUCCESS: Solution submitted successfully!");
            log.info("Response: {}", response);
            log.info("=".repeat(60));

        } catch (WebClientResponseException e) {
            log.error("ERROR: Failed to submit solution");
            log.error("  - Status: {}", e.getStatusCode());
            log.error("  - Response: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Failed to submit solution: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("ERROR: Unexpected error during solution submission: {}", e.getMessage());
            throw new RuntimeException("Failed to submit solution: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts last two digits from registration number.
     * Handles both numeric and alphanumeric registration numbers.
     */
    private int getLastTwoDigits(String regNo) {
        if (regNo == null || regNo.isEmpty()) {
            return 0;
        }
        
        // Extract only digits from the regNo
        String digitsOnly = regNo.replaceAll("[^0-9]", "");
        
        if (digitsOnly.isEmpty()) {
            return 0;
        }
        
        // Get last two digits
        int length = digitsOnly.length();
        String lastTwo = length >= 2 
            ? digitsOnly.substring(length - 2) 
            : digitsOnly;
        
        return Integer.parseInt(lastTwo);
    }

    /**
     * Returns the final SQL query for reference.
     */
    public String getFinalSqlQuery() {
        return FINAL_SQL_QUERY;
    }
}

