package com.tcs.rcs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tcs.rcs.entity.FormSubmission;
import com.tcs.rcs.entity.User;
import com.tcs.rcs.repository.FormSubmissionRepository;
import com.tcs.rcs.repository.UserRepository;

@Component("submissionSecurity")
public class SubmissionSecurity {
	@Autowired
	private FormSubmissionRepository submissionRepository;
	@Autowired
	private UserRepository userRepository;

	public boolean canEdit(Long submissionId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			return false;
		}
		String username = auth.getName();
		User currentUser = userRepository.findByUserName(username).orElse(null);
		if (currentUser == null) {
			return false;
		}

		// Admin, TPM, Sales can edit all
		if (currentUser.getRoles().stream().anyMatch(
				r -> r.getName().equals("ADMIN") || r.getName().equals("TPM") || r.getName().equals("SALES"))) {
			return true;
		}

		// Customers can only edit their own submissions
		FormSubmission submission = submissionRepository.findById(submissionId).orElse(null);
		if (submission == null) {
			return false;
		}

		return submission.getCreatedBy() != null && submission.getCreatedBy().getId().equals(currentUser.getId());
	}
}
