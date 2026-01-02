package com.tcs.rcs.dto;

import java.time.LocalDateTime;

public record SubmissionSummaryDto(Long id, Long customerId, String status, LocalDateTime createdAt) {

}
