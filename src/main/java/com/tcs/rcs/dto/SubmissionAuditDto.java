package com.tcs.rcs.dto;

import java.time.LocalDateTime;

public record SubmissionAuditDto(Long id, Long submissionId, String oldStatus, String newStatus, String actor,
		String remarks, LocalDateTime timestamp) {

}
