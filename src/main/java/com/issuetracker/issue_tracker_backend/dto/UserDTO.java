package com.issuetracker.issue_tracker_backend.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String password;
}