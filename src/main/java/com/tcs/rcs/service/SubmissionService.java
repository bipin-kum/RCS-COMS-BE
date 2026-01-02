package com.tcs.rcs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tcs.rcs.dto.CreateSubmissionDto;
import com.tcs.rcs.dto.CreatedUserDto;
import com.tcs.rcs.dto.ReviewActionDto;
import com.tcs.rcs.dto.SubmissionDto;
import com.tcs.rcs.dto.SubmissionFilter;
import com.tcs.rcs.dto.SubmissionSummaryDto;
import com.tcs.rcs.dto.UpdateSubmissionDto;
import com.tcs.rcs.entity.Customer;
import com.tcs.rcs.entity.FormSubmission;
import com.tcs.rcs.entity.FormTemplate;
import com.tcs.rcs.entity.SubmissionAudit;
import com.tcs.rcs.entity.User;
import com.tcs.rcs.enums.SubmissionStatus;
import com.tcs.rcs.repository.CustomerRepository;
import com.tcs.rcs.repository.FormSubmissionRepository;
import com.tcs.rcs.repository.FormTemplateRepository;
import com.tcs.rcs.repository.SubmissionAuditRepository;
import com.tcs.rcs.repository.UserRepository;
import com.tcs.rcs.specification.SubmissionSpecifications;

@Service
public class SubmissionService {

	@Autowired
	private FormSubmissionRepository submissionRepository;
	@Autowired
	private FormTemplateRepository templateRepository;
	@Autowired
	private SubmissionAuditRepository auditRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FormValidator formValidator;
	@Autowired
	private CustomerRepository customerRepository;

	public SubmissionDto create(CreateSubmissionDto dto) {
		// Get Form Template and validate with data json
		FormTemplate template = templateRepository.findById(dto.formTemplateId())
				.orElseThrow(() -> new IllegalArgumentException("Template not found"));
		formValidator.validate(template.getSchemaJson(), dto.dataJson());

		// Get User details
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String creatorUsername = auth.getName();
		User creator = userRepository.findByUserName(creatorUsername).orElseThrow();

		Customer customer = customerRepository.findById(dto.customerId())
				.orElseThrow(() -> new IllegalArgumentException("Customer not found"));

		// Persist submission with status (DRAFT or SUBMITTED)
		FormSubmission formSubmission = new FormSubmission();
		formSubmission.setCustomer(customer);
		formSubmission.setDataJson(dto.dataJson());
		formSubmission.setStatus(SubmissionStatus.valueOf(dto.status()));
		formSubmission.setTemplate(template);
		formSubmission.setCreatedBy(creator);
		FormSubmission savedFormSubmission = submissionRepository.save(formSubmission);

		// Record audit entry
		SubmissionAudit submissionAudit = new SubmissionAudit();
		submissionAudit.setSubmission(savedFormSubmission);
		submissionAudit.setActor(creator);
		submissionAudit.setOldStatus(formSubmission.getStatus());
		submissionAudit.setNewStatus(savedFormSubmission.getStatus());
		auditRepository.save(submissionAudit);

		return new SubmissionDto(savedFormSubmission.getId(), savedFormSubmission.getTemplate().getId(),
				savedFormSubmission.getCustomer().getId(), savedFormSubmission.getStatus().name(),
				savedFormSubmission.getDataJson(),
				new CreatedUserDto(savedFormSubmission.getCreatedBy().getId(),
						savedFormSubmission.getCreatedBy().getUserName()),
				savedFormSubmission.getCreatedAt(), new CreatedUserDto(savedFormSubmission.getUpdatedBy().getId(),
						savedFormSubmission.getUpdatedBy().getUserName()),
				savedFormSubmission.getUpdatedAt());
	}

	public SubmissionDto update(Long id, UpdateSubmissionDto dto) {
		// Fetch submission
		FormSubmission formSubmission = submissionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Form Submission not found"));

		// Get Form Template and validate with data json
		FormTemplate template = templateRepository.findById(formSubmission.getTemplate().getId())
				.orElseThrow(() -> new IllegalArgumentException("Template not found"));
		formValidator.validate(template.getSchemaJson(), dto.dataJson());

		// Validate with new Status
		SubmissionStatus newStatus = formSubmission.getStatus();
		if (!(newStatus == SubmissionStatus.DRAFT || newStatus == SubmissionStatus.SUBMITTED)) {
			throw new IllegalArgumentException("Form Submission can not be done with status" + newStatus.name());
		}

		// Get User details
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String creatorUsername = auth.getName();
		User creator = userRepository.findByUserName(creatorUsername).orElseThrow();

		SubmissionStatus oldStatus = formSubmission.getStatus();

		// Update submission, save, record audit
		formSubmission.setDataJson(dto.dataJson());
		formSubmission.setStatus(SubmissionStatus.valueOf(dto.status()));
		formSubmission.setUpdatedBy(creator);
		FormSubmission savedFormSubmission = submissionRepository.save(formSubmission);

		// Record audit entry
		SubmissionAudit submissionAudit = new SubmissionAudit();
		submissionAudit.setSubmission(formSubmission);
		submissionAudit.setActor(creator);
		submissionAudit.setOldStatus(oldStatus);
		submissionAudit.setNewStatus(formSubmission.getStatus());
		auditRepository.save(submissionAudit);

		return new SubmissionDto(savedFormSubmission.getId(), savedFormSubmission.getTemplate().getId(),
				savedFormSubmission.getCustomer().getId(), savedFormSubmission.getStatus().name(),
				savedFormSubmission.getDataJson(),
				new CreatedUserDto(savedFormSubmission.getCreatedBy().getId(),
						savedFormSubmission.getCreatedBy().getUserName()),
				savedFormSubmission.getCreatedAt(), new CreatedUserDto(savedFormSubmission.getUpdatedBy().getId(),
						savedFormSubmission.getUpdatedBy().getUserName()),
				savedFormSubmission.getUpdatedAt());
	}

	public SubmissionDto review(Long id, ReviewActionDto dto) {
		// Fetch submission
		FormSubmission formSubmission = submissionRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Form Submission not found"));

		// Validate with old Status
		SubmissionStatus oldStatus = formSubmission.getStatus();
		if (oldStatus != SubmissionStatus.IN_REVIEW) {
			throw new IllegalArgumentException(
					"Form Review can not be done Form is not in IN-REVIEW. Current status is " + oldStatus);
		}

		// Validate with new Status
		SubmissionStatus newStatus = SubmissionStatus.valueOf(dto.action());
		if (!(newStatus == SubmissionStatus.APPROVED || newStatus == SubmissionStatus.REJECTED)) {
			throw new IllegalArgumentException("Form Review can not be done with status" + newStatus.name());
		}

		// Get User details
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String creatorUsername = auth.getName();
		User creator = userRepository.findByUserName(creatorUsername).orElseThrow();

		formSubmission.setStatus(SubmissionStatus.valueOf(dto.action()));
		formSubmission.setUpdatedBy(creator);
		formSubmission.setRemarks(dto.remarks());
		FormSubmission savedFormSubmission = submissionRepository.save(formSubmission);

		// Record audit entry
		SubmissionAudit submissionAudit = new SubmissionAudit();
		submissionAudit.setSubmission(formSubmission);
		submissionAudit.setActor(creator);
		submissionAudit.setOldStatus(oldStatus);
		submissionAudit.setNewStatus(formSubmission.getStatus());
		submissionAudit.setRemarks(dto.remarks());
		auditRepository.save(submissionAudit);

		return new SubmissionDto(savedFormSubmission.getId(), savedFormSubmission.getTemplate().getId(),
				savedFormSubmission.getCustomer().getId(), savedFormSubmission.getStatus().name(),
				savedFormSubmission.getDataJson(),
				new CreatedUserDto(savedFormSubmission.getCreatedBy().getId(),
						savedFormSubmission.getCreatedBy().getUserName()),
				savedFormSubmission.getCreatedAt(), new CreatedUserDto(savedFormSubmission.getUpdatedBy().getId(),
						savedFormSubmission.getUpdatedBy().getUserName()),
				savedFormSubmission.getUpdatedAt());
	}

	public Page<SubmissionSummaryDto> search(SubmissionFilter filter, Pageable pageable) {
		Page<FormSubmission> submissions = submissionRepository.findAll(SubmissionSpecifications.withFilter(filter),
				pageable);
		return submissions.map(sub -> new SubmissionSummaryDto(sub.getId(), sub.getCustomer().getId(),
				sub.getStatus().name(), sub.getCreatedAt()));
	}
}
