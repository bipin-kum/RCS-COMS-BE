package com.tcs.rcs.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tcs.rcs.entity.FormSubmission;
import com.tcs.rcs.enums.SubmissionStatus;

public interface FormSubmissionRepository
		extends JpaRepository<FormSubmission, Long>, JpaSpecificationExecutor<FormSubmission> {
	List<FormSubmission> findByCustomerId(Long customerId);

	List<FormSubmission> findByStatus(SubmissionStatus status);

	List<FormSubmission> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
