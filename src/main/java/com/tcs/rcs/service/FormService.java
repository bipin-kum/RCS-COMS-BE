package com.tcs.rcs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tcs.rcs.dto.CreateFormTemplateDto;
import com.tcs.rcs.dto.CreatedUserDto;
import com.tcs.rcs.dto.FormTemplateDto;
import com.tcs.rcs.dto.FormTemplateSummaryDto;
import com.tcs.rcs.entity.FormTemplate;
import com.tcs.rcs.entity.User;
import com.tcs.rcs.enums.FormType;
import com.tcs.rcs.repository.FormTemplateRepository;
import com.tcs.rcs.repository.UserRepository;

@Service
public class FormService {
	@Autowired
	private FormTemplateRepository formTemplateRepository;
	@Autowired
	private UserRepository userRepository;

	public FormTemplateDto create(CreateFormTemplateDto dto) {
		// Validate version uniqueness per type
		formTemplateRepository.findByTypeOrderByVersionDesc(FormType.valueOf(dto.type())).stream()
				.filter(t -> t.getVersion().equals(dto.version())).findAny().ifPresent(t -> {
					throw new IllegalArgumentException("Version already exists for type " + dto.type());
				});

		// Get User details
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String creatorUsername = auth.getName();
		User creator = userRepository.findByUserName(creatorUsername).orElseThrow();

		FormTemplate template = new FormTemplate();
		template.setType(FormType.valueOf(dto.type()));
		template.setVersion(dto.version());
		template.setSchemaJson(dto.schemaJson());
		template.setCreatedBy(creator);
		FormTemplate savedTemplate = formTemplateRepository.save(template);

		return new FormTemplateDto(savedTemplate.getId(), savedTemplate.getType().name(), savedTemplate.getVersion(),
				savedTemplate.getSchemaJson(), savedTemplate.getIsActive(),
				new CreatedUserDto(savedTemplate.getCreatedBy().getId(), savedTemplate.getCreatedBy().getUserName()),
				savedTemplate.getCreatedAt());
	}

	public List<FormTemplateSummaryDto> list(FormType type, Boolean latest) {
		if (Boolean.TRUE.equals(latest)) {
			return formTemplateRepository.findTopByTypeOrderByVersionDesc(type).map(
					t -> new FormTemplateSummaryDto(t.getId(), t.getType().name(), t.getVersion(), t.getIsActive()))
					.stream().toList();
		}
		return formTemplateRepository.findByTypeOrderByVersionDesc(type).stream()
				.map(t -> new FormTemplateSummaryDto(t.getId(), t.getType().name(), t.getVersion(), t.getIsActive()))
				.collect(Collectors.toList());
	}

	public FormTemplateDto get(Long id) {
		FormTemplate template = formTemplateRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("FormTemplate not found: " + id));
		return new FormTemplateDto(template.getId(), template.getType().name(), template.getVersion(),
				template.getSchemaJson(), template.getIsActive(),
				new CreatedUserDto(template.getCreatedBy().getId(), template.getCreatedBy().getUserName()),
				template.getCreatedAt());
	}
}
