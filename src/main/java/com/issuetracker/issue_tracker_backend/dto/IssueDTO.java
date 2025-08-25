package com.issuetracker.issue_tracker_backend.dto;

import com.issuetracker.issue_tracker_backend.entity.Issue;
import lombok.Data;

@Data
public class IssueDTO {
    private String title;
    private String description;
    private Issue.IssueStatus status;
    private Issue.IssuePriority priority;
}