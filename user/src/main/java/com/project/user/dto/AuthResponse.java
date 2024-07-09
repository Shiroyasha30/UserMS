package com.project.user.dto;

import com.project.user.Entity.User;

public record AuthResponse(User user, String token) {
}
