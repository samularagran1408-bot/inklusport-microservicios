package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.AttendanceRequest;
import com.inklusport.sports.dto.response.AttendanceResponse;
import com.inklusport.sports.entity.EventAttendance;
import com.inklusport.sports.entity.EventRegistration;
import com.inklusport.sports.repository.EventAttendanceRepository;
import com.inklusport.sports.repository.EventRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventAttendanceService {

    private final EventAttendanceRepository attendanceRepository;
    private final EventRegistrationRepository registrationRepository;

    @Transactional
    public AttendanceResponse checkInUser(AttendanceRequest request) {
        EventRegistration registration = registrationRepository.findById(request.getRegistrationId())
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        // Marcar asistencia en la inscripción
        registration.setAttended(true);
        registrationRepository.save(registration);

        EventAttendance attendance = EventAttendance.builder()
                .registrationId(registration.getId())
                .verifiedBy(request.getVerifiedBy())
                .build();

        if (request.getCheckInMethod() != null) {
            attendance.setCheckInMethod(EventAttendance.CheckInMethod.valueOf(request.getCheckInMethod().toLowerCase()));
        }

        return convertToResponse(attendanceRepository.save(attendance));
    }

    private AttendanceResponse convertToResponse(EventAttendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .registrationId(attendance.getRegistrationId())
                .checkInTime(attendance.getCheckInTime())
                .checkInMethod(attendance.getCheckInMethod().name())
                .verifiedBy(attendance.getVerifiedBy())
                .build();
    }
}