package com.bajaj.qualifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Bajaj Finserv Health Qualifier 1 - JAVA
 * 
 * This application automatically:
 * 1. Sends POST request to generate webhook on startup
 * 2. Extracts webhook URL and accessToken
 * 3. Submits SQL query solution to the webhook
 * 
 * @author Bajaj Qualifier Submission
 */
@SpringBootApplication
public class BajajQualifierApplication {

    public static void main(String[] args) {
        SpringApplication.run(BajajQualifierApplication.class, args);
    }
}

