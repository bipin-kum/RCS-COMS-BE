package com.tcs.rcs.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.tcs.rcs.dto.SubmissionFilter;
import com.tcs.rcs.entity.FormSubmission;

import jakarta.persistence.criteria.Predicate;

public class SubmissionSpecifications {
	public static Specification<FormSubmission> withFilter(SubmissionFilter filter) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (filter.getCustomerId() != null) {
				predicates.add(cb.equal(root.join("customer").get("id"), filter.getCustomerId()));
			}
			if (filter.getStatus() != null) {
				predicates.add(cb.equal(root.get("status"), filter.getStatus()));
			}
			if (filter.getStartDate() != null && filter.getEndDate() != null) {
				predicates.add(cb.between(root.get("createdAt"), filter.getStartDate(), filter.getEndDate()));
			} else if (filter.getStartDate() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getStartDate()));
			} else if (filter.getEndDate() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getEndDate()));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
