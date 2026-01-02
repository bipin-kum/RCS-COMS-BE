package com.tcs.rcs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.rcs.dto.SubmissionAuditDto;
import com.tcs.rcs.service.AuditService;

@RestController
@RequestMapping("/api/submissions")
public class AuditController {
	@Autowired
	private AuditService auditService;

	@GetMapping("/{id}/audit")
	@PreAuthorize("hasAnyRole('TPM','SALES','ADMIN','CUSTOMER')")
	public List<SubmissionAuditDto> getAuditTrail(@PathVariable Long id) {
		return auditService.getAuditTrail(id);
	}
}
