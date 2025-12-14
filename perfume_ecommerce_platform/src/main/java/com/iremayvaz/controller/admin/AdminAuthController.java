package com.iremayvaz.controller.admin;

import com.iremayvaz.model.entity.User;
import com.iremayvaz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "admin/login";
    }

    @GetMapping("/me")
    public String me(Authentication authentication, Model model) {
        return "admin/me";
    }
}
