package com.tcs.rcs.dto;

public record CreateSubmissionDto(Long formTemplateId, Long customerId, String dataJson,
		String status /* DRAFT or SUBMITTED */
) {
}
