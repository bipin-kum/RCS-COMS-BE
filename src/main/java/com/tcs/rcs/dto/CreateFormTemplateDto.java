package com.tcs.rcs.dto;

public record CreateFormTemplateDto(String type /* ORDER or QUALIFICATION */, Integer version, String schemaJson) {
}
