package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.DisabilityRequest;
import com.inklusport.sports.dto.response.DisabilityResponse;
import com.inklusport.sports.entity.Disability;
import com.inklusport.sports.repository.DisabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisabilityService {

    private final DisabilityRepository disabilityRepository;

    @Transactional(readOnly = true)
    public List<DisabilityResponse> findAll() {
        return disabilityRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DisabilityResponse findById(String id) {
        return toResponse(getById(id));
    }

    @Transactional
    public DisabilityResponse create(DisabilityRequest request) {
        Disability disability = new Disability();
        disability.setName(request.getName());
        disability.setDescription(request.getDescription());
        return toResponse(disabilityRepository.save(disability));
    }

    @Transactional
    public DisabilityResponse update(String id, DisabilityRequest request) {
        Disability disability = getById(id);
        disability.setName(request.getName());
        disability.setDescription(request.getDescription());
        return toResponse(disabilityRepository.save(disability));
    }

    @Transactional
    public void delete(String id) {
        if (!disabilityRepository.existsById(id)) {
            throw new RuntimeException("Discapacidad no encontrada");
        }
        disabilityRepository.deleteById(id);
    }

    private Disability getById(String id) {
        return disabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discapacidad no encontrada"));
    }

    private DisabilityResponse toResponse(Disability disability) {
        return DisabilityResponse.builder()
                .id(disability.getId())
                .name(disability.getName())
                .description(disability.getDescription())
                .createdAt(disability.getCreatedAt())
                .build();
    }
}
