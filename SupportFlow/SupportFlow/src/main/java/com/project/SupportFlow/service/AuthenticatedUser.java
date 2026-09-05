package com.project.SupportFlow.service;

import com.project.SupportFlow.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    public User getUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public Long getUserId() {
        return getUser().getId();
    }
}
