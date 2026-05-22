package com.inklusport.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "users-ms", url = "${users.service.url}")
public interface UserServiceClient {

    @GetMapping("/api/internal/users/roles-by-email")
    List<String> getUserRoles(@RequestParam("email") String email);
}