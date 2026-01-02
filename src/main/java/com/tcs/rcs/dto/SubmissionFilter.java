package com.tcs.rcs.dto;

import java.time.LocalDateTime;

import com.tcs.rcs.enums.SubmissionStatus;

public class SubmissionFilter {
	private Long customerId; // filter by customer
	private SubmissionStatus status; // filter by status (DRAFT, SUBMITTED, etc.)
	private LocalDateTime startDate; // filter by createdAt >= startDate
	private LocalDateTime endDate; // filter by createdAt <= endDate

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public SubmissionStatus getStatus() {
		return status;
	}

	public void setStatus(SubmissionStatus status) {
		this.status = status;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
}
