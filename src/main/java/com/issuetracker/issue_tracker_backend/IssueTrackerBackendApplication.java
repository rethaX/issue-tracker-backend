package com.issuetracker.issue_tracker_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.issuetracker.issue_tracker_backend.entity")
@EnableJpaRepositories(basePackages = "com.issuetracker.issue_tracker_backend.repository")
public class IssueTrackerBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(IssueTrackerBackendApplication.class, args);
	}
}