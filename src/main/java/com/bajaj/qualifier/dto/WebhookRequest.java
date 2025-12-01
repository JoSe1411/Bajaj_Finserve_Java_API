package com.bajaj.qualifier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("regNo")
    private String regNo;

    @JsonProperty("email")
    private String email;
}

