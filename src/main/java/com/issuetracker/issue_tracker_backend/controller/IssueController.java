package com.issuetracker.issue_tracker_backend.controller;

import com.issuetracker.issue_tracker_backend.dto.IssueDTO;
import com.issuetracker.issue_tracker_backend.entity.Issue;
import com.issuetracker.issue_tracker_backend.entity.User;
import com.issuetracker.issue_tracker_backend.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @PostMapping
    public ResponseEntity<Issue> createIssue(@RequestBody IssueDTO issueDTO) {
        User user = getCurrentUser();
        Issue issue = issueService.createIssue(issueDTO, user);
        return ResponseEntity.ok(issue);
    }

    @GetMapping
    public ResponseEntity<List<Issue>> getIssues(@RequestParam(required = false) Issue.IssueStatus status,
                                                 @RequestParam(required = false) Issue.IssuePriority priority) {
        User user = getCurrentUser();
        List<Issue> issues = issueService.getIssuesByUser(user, status, priority);
        return ResponseEntity.ok(issues);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Issue> updateIssue(@PathVariable UUID id, @RequestBody IssueDTO issueDTO) {
        User user = getCurrentUser();
        Issue updatedIssue = issueService.updateIssue(id, issueDTO, user);
        return ResponseEntity.ok(updatedIssue);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();  // Assumes User is principal; adjust if needed
    }
}