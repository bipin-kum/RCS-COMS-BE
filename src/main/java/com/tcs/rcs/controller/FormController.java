package com.tcs.rcs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.rcs.dto.CreateFormTemplateDto;
import com.tcs.rcs.dto.FormTemplateDto;
import com.tcs.rcs.dto.FormTemplateSummaryDto;
import com.tcs.rcs.enums.FormType;
import com.tcs.rcs.service.FormService;

@RestController
@RequestMapping("/api/forms")
public class FormController {

	@Autowired
	private FormService formService;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public FormTemplateDto create(@RequestBody CreateFormTemplateDto dto) {
		return formService.create(dto);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','TPM','SALES','CUSTOMER')")
	public List<FormTemplateSummaryDto> list(@RequestParam FormType type,
			@RequestParam(required = false) Boolean latest) {
		return formService.list(type, latest);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','TPM','SALES','CUSTOMER')")
	public FormTemplateDto get(@PathVariable Long id) {
		return formService.get(id);
	}
}
