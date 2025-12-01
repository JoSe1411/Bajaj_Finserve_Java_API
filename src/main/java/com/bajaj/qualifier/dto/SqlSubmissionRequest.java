package com.bajaj.qualifier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for submitting SQL query solution.
 * Contains the final SQL query to be submitted.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlSubmissionRequest {

    @JsonProperty("finalQuery")
    private String finalQuery;
}

