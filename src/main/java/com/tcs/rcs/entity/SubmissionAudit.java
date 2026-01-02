package com.tcs.rcs.entity;

import java.time.LocalDateTime;

import com.tcs.rcs.enums.SubmissionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "submission_audits")
public class SubmissionAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "submission_id", nullable = false)
	private FormSubmission submission;

	@Enumerated(EnumType.STRING)
	private SubmissionStatus oldStatus;

	@Enumerated(EnumType.STRING)
	private SubmissionStatus newStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "actor_id", nullable = false)
	private User actor;

	@Column(columnDefinition = "TEXT")
	private String remarks;

	@Column(nullable = false)
	private LocalDateTime timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FormSubmission getSubmission() {
		return submission;
	}

	public void setSubmission(FormSubmission submission) {
		this.submission = submission;
	}

	public SubmissionStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(SubmissionStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	public SubmissionStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(SubmissionStatus newStatus) {
		this.newStatus = newStatus;
	}

	public User getActor() {
		return actor;
	}

	public void setActor(User actor) {
		this.actor = actor;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@PrePersist
	void pre() {
		timestamp = LocalDateTime.now();
	}
}
