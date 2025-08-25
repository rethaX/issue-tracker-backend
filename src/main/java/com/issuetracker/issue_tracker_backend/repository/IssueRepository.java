package com.issuetracker.issue_tracker_backend.repository;

import com.issuetracker.issue_tracker_backend.entity.Issue.IssuePriority;
import com.issuetracker.issue_tracker_backend.entity.Issue.IssueStatus;
import com.issuetracker.issue_tracker_backend.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IssueRepository extends JpaRepository<Issue, UUID> {
    List<Issue> findByUserId(UUID userId);

    @Query("SELECT i FROM Issue i WHERE i.user.id = :userId AND (:status IS NULL OR i.status = :status) AND (:priority IS NULL OR i.priority = :priority)")
    List<Issue> findByUserIdAndFilters(@Param("userId") UUID userId, @Param("status") IssueStatus status, @Param("priority") IssuePriority priority);
}