package com.tcs.rcs.dto;

import java.time.LocalDateTime;

public record SubmissionDto(Long id, Long formTemplateId, Long customerId, String status, String dataJson,
		CreatedUserDto createdBy, LocalDateTime createdAt, CreatedUserDto updatedBy, LocalDateTime updatedAt) {

}
