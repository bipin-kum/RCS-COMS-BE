package com.tcs.rcs.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.rcs.dto.CreateSubmissionDto;
import com.tcs.rcs.dto.ReviewActionDto;
import com.tcs.rcs.dto.SubmissionDto;
import com.tcs.rcs.dto.SubmissionFilter;
import com.tcs.rcs.dto.SubmissionSummaryDto;
import com.tcs.rcs.dto.UpdateSubmissionDto;
import com.tcs.rcs.enums.SubmissionStatus;
import com.tcs.rcs.service.SubmissionService;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {
	@Autowired
	private SubmissionService submissionService;

	@PostMapping
	@PreAuthorize("hasAnyRole('CUSTOMER','TPM','SALES')")
	public SubmissionDto create(@RequestBody CreateSubmissionDto dto) {
		return submissionService.create(dto);
	}

	@PutMapping("/{id}")
	@PreAuthorize("@submissionSecurity.canEdit(#id)")
	public SubmissionDto update(@PathVariable Long id, @RequestBody UpdateSubmissionDto dto) {
		return submissionService.update(id, dto);
	}

	@PostMapping("/{id}/review")
	@PreAuthorize("hasAnyRole('TPM','SALES','ADMIN')")
	public SubmissionDto review(@PathVariable Long id, @RequestBody ReviewActionDto dto) {
		return submissionService.review(id, dto);
	}

	@GetMapping("/search")
	@PreAuthorize("hasAnyRole('TPM','SALES','ADMIN','CUSTOMER')")
	public Page<SubmissionSummaryDto> search(@RequestParam(required = false) Long customerId,
			@RequestParam(required = false) String status, @RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate, Pageable pageable) {
		SubmissionFilter filter = new SubmissionFilter();
		filter.setCustomerId(customerId);
		if (status != null)
			filter.setStatus(Enum.valueOf(SubmissionStatus.class, status));
		if (startDate != null)
			filter.setStartDate(LocalDateTime.parse(startDate));
		if (endDate != null)
			filter.setEndDate(LocalDateTime.parse(endDate));
		return submissionService.search(filter, pageable);
	}
}
