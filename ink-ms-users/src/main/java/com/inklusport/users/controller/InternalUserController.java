package com.inklusport.users.controller;

import com.inklusport.users.dto.response.UserProfileResponse;
import com.inklusport.users.service.RoleService;
import com.inklusport.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final RoleService roleService;

    @GetMapping("/roles-by-email")
    public List<String> getUserRoles(@RequestParam String email) {
        return roleService.getUserRoles(email);
    }
}
