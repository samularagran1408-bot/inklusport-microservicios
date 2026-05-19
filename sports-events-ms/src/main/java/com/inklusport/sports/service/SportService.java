package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.SportRequest;
import com.inklusport.sports.dto.response.SportResponse;
import com.inklusport.sports.entity.Disability;
import com.inklusport.sports.entity.Sport;
import com.inklusport.sports.entity.SportDisability;
import com.inklusport.sports.repository.DisabilityRepository;
import com.inklusport.sports.repository.SportDisabilityRepository;
import com.inklusport.sports.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;
    private final DisabilityRepository disabilityRepository;
    private final SportDisabilityRepository sportDisabilityRepository;

    @Transactional(readOnly = true)
    public List<SportResponse> findAll() {
        return sportRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SportResponse findById(String id) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        return toResponse(sport);
    }

    @Transactional
    public SportResponse create(SportRequest request) {
        Sport sport = new Sport();
        sport.setName(request.getName());
        sport.setDescription(request.getDescription());
        return toResponse(sportRepository.save(sport));
    }

    @Transactional
    public SportResponse update(String id, SportRequest request) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        sport.setName(request.getName());
        sport.setDescription(request.getDescription());
        return toResponse(sportRepository.save(sport));
    }

    @Transactional
    public void delete(String id) {
        if (!sportRepository.existsById(id)) {
            throw new RuntimeException("Deporte no encontrado");
        }
        sportRepository.deleteById(id);
    }

    @Transactional
    public SportResponse linkDisability(String sportId, String disabilityId) {
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        Disability disability = disabilityRepository.findById(disabilityId)
                .orElseThrow(() -> new RuntimeException("Discapacidad no encontrada"));
        if (sportDisabilityRepository.existsBySport_IdAndDisability_Id(sportId, disabilityId)) {
            throw new RuntimeException("La relación ya existe");
        }
        SportDisability link = new SportDisability();
        link.setSport(sport);
        link.setDisability(disability);
        sportDisabilityRepository.save(link);
        return toResponse(sport);
    }

    private SportResponse toResponse(Sport sport) {
        List<String> disabilityIds = sportDisabilityRepository.findBySport_Id(sport.getId()).stream()
                .map(sd -> sd.getDisability().getId())
                .toList();
        return SportResponse.builder()
                .id(sport.getId())
                .name(sport.getName())
                .description(sport.getDescription())
                .createdAt(sport.getCreatedAt())
                .disabilityIds(disabilityIds)
                .build();
    }
}
