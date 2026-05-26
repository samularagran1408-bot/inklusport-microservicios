package com.inklusport.sports.controller;

import com.inklusport.sports.dto.request.AttendanceRequest;
import com.inklusport.sports.dto.response.AttendanceResponse;
import com.inklusport.sports.service.EventAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class EventAttendanceController {

    private final EventAttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> checkIn(@RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.checkInUser(request));
    }
}