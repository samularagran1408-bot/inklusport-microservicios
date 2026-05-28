package com.inklusport.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "ink-ms-users", url = "${users.service.url:http://localhost:3002}")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    Map<String, Object> getUserById(@PathVariable("id") String id);

    @GetMapping("/api/internal/users/id-by-email")
    Map<String, String> getUserIdByEmail(@RequestParam("email") String email);

    @PostMapping("/api/users/{id}/disable")
    Map<String, Object> disableUser(@PathVariable("id") String id, 
                                     @RequestParam("reason") String reason);

    @PostMapping("/api/users/{id}/enable")
    Map<String, Object> enableUser(@PathVariable("id") String id);
}