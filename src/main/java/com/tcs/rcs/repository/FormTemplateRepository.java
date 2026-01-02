package com.tcs.rcs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.rcs.entity.FormTemplate;
import com.tcs.rcs.enums.FormType;

public interface FormTemplateRepository extends JpaRepository<FormTemplate, Long> {
	Optional<FormTemplate> findTopByTypeOrderByVersionDesc(FormType type);

	List<FormTemplate> findByTypeOrderByVersionDesc(FormType type);
}
