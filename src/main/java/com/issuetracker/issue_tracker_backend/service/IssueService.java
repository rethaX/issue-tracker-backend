package com.issuetracker.issue_tracker_backend.service;

import com.issuetracker.issue_tracker_backend.dto.IssueDTO;
import com.issuetracker.issue_tracker_backend.entity.Issue;
import com.issuetracker.issue_tracker_backend.entity.User;
import com.issuetracker.issue_tracker_backend.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    public Issue createIssue(IssueDTO issueDTO, User user) {
        Issue issue = new Issue();
        issue.setTitle(issueDTO.getTitle());
        issue.setDescription(issueDTO.getDescription());
        issue.setStatus(issueDTO.getStatus());
        issue.setPriority(issueDTO.getPriority());
        issue.setUser(user);
        issue.setUpdatedAt(Instant.now());
        return issueRepository.save(issue);
    }

    public List<Issue> getIssuesByUser(User user, Issue.IssueStatus status, Issue.IssuePriority priority) {
        return issueRepository.findByUserIdAndFilters(user.getId(), status, priority);
    }

    public Issue updateIssue(UUID id, IssueDTO issueDTO, User user) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> new RuntimeException("Issue not found"));
        if (!issue.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this issue");
        }

        if (issueDTO.getTitle() != null) issue.setTitle(issueDTO.getTitle());
        if (issueDTO.getDescription() != null) issue.setDescription(issueDTO.getDescription());
        if (issueDTO.getStatus() != null) issue.setStatus(issueDTO.getStatus());
        if (issueDTO.getPriority() != null) issue.setPriority(issueDTO.getPriority());
        issue.setUpdatedAt(Instant.now());
        return issueRepository.save(issue);
    }
}