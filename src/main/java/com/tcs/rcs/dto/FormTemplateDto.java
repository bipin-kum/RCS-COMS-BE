package com.tcs.rcs.dto;

import java.time.LocalDateTime;

public record FormTemplateDto(Long id, String type, Integer version, String schemaJson, Boolean isActive,
		CreatedUserDto createdBy, LocalDateTime createdAt) {
}
