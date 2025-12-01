package com.bajaj.qualifier.runner;

import com.bajaj.qualifier.dto.WebhookResponse;
import com.bajaj.qualifier.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Startup Runner that executes automatically when the application starts.
 * This ensures no manual controller invocation is needed.
 * 
 * Flow:
 * 1. Generate webhook (POST request)
 * 2. Extract webhook URL and access token
 * 3. Submit SQL solution
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final WebhookService webhookService;

    @Override
    public void run(String... args) {
        log.info("");
        log.info("╔══════════════════════════════════════════════════════════════╗");
        log.info("║     BAJAJ FINSERV HEALTH - QUALIFIER 1 (JAVA)                ║");
        log.info("║     Automated Webhook Submission System                       ║");
        log.info("╚══════════════════════════════════════════════════════════════╝");
        log.info("");

        try {
            // Step 1: Generate Webhook
            log.info("STEP 1: Generating Webhook...");
            WebhookResponse webhookResponse = webhookService.generateWebhook();

            if (webhookResponse == null) {
                log.error("FAILED: Webhook response is null");
                return;
            }

            if (webhookResponse.getWebhook() == null || webhookResponse.getAccessToken() == null) {
                log.error("FAILED: Missing webhook URL or access token in response");
                log.error("  - Webhook: {}", webhookResponse.getWebhook());
                log.error("  - AccessToken: {}", webhookResponse.getAccessToken());
                return;
            }

            // Step 2: Submit Solution
            log.info("");
            log.info("STEP 2: Submitting SQL Solution...");
            webhookService.submitSolution(
                webhookResponse.getWebhook(), 
                webhookResponse.getAccessToken()
            );

            log.info("");
            log.info("╔══════════════════════════════════════════════════════════════╗");
            log.info("║     ✓ ALL STEPS COMPLETED SUCCESSFULLY!                      ║");
            log.info("╚══════════════════════════════════════════════════════════════╝");
            log.info("");

            // Print final SQL query for verification
            log.info("FINAL SQL QUERY SUBMITTED:");
            log.info("-".repeat(60));
            log.info(webhookService.getFinalSqlQuery());
            log.info("-".repeat(60));

        } catch (Exception e) {
            log.error("");
            log.error("╔══════════════════════════════════════════════════════════════╗");
            log.error("║     ✗ EXECUTION FAILED                                       ║");
            log.error("╚══════════════════════════════════════════════════════════════╝");
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}

