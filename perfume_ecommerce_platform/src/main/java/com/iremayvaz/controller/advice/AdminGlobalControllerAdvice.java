package com.iremayvaz.controller.advice;

import com.iremayvaz.model.entity.User;
import com.iremayvaz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.iremayvaz.controller.admin")
@RequiredArgsConstructor
public class AdminGlobalControllerAdvice {

    private final UserService userService;

    @ModelAttribute("adminUser")
    public User addAdminUserToModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) return null;
        if(!authentication.isAuthenticated()) return null;
        if(authentication instanceof AnonymousAuthenticationToken) return null;

        String email = authentication.getName();
        if(email == null || email.equals("anonymousUser")) return null;

        return userService.findByEmailOrNull(email);
    }

    @ModelAttribute("adminInitials")
    public String adminInitials(@ModelAttribute("admin") User user) {
        if (user == null) return "A";

        String first = safeFirstChar(user.getFirstName());
        String last  = safeFirstChar(user.getLastName());

        String initials = (first + last).toUpperCase();
        return initials.isBlank() ? "A" : initials;
    }

    private String safeFirstChar(String s) {
        if (s == null) return "";
        s = s.trim();
        return s.isEmpty() ? "" : s.substring(0, 1);
    }
}
