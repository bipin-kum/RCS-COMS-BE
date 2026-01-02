package com.tcs.rcs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.rcs.entity.SubmissionAudit;

public interface SubmissionAuditRepository extends JpaRepository<SubmissionAudit, Long> {
	List<SubmissionAudit> findBySubmissionIdOrderByTimestampAsc(Long submissionId);
}
