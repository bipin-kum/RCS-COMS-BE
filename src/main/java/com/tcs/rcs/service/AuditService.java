package com.tcs.rcs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.rcs.dto.SubmissionAuditDto;
import com.tcs.rcs.repository.SubmissionAuditRepository;

@Service
public class AuditService {
	@Autowired
	private SubmissionAuditRepository auditRepository;

	public List<SubmissionAuditDto> getAuditTrail(Long submissionId) {
		return auditRepository.findBySubmissionIdOrderByTimestampAsc(submissionId).stream()
				.map(a -> new SubmissionAuditDto(a.getId(), a.getSubmission().getId(), a.getOldStatus().name(),
						a.getNewStatus().name(), a.getActor().getUserName(), a.getRemarks(), a.getTimestamp()))
				.toList();
	}
}
