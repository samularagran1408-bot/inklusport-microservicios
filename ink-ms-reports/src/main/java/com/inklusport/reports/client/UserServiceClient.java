package com.inklusport.reports.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "users-ms", url = "${users.service.url:http://localhost:3002}")
public interface UserServiceClient {

    @GetMapping("/api/admin/users/count")
    int getTotalUsers(@RequestHeader("Authorization") String authorization);
}