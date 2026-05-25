package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.DisabilityRequest;
import com.inklusport.sports.dto.response.DisabilityResponse;
import com.inklusport.sports.entity.Disability;
import com.inklusport.sports.repository.DisabilityRepository;
import com.inklusport.sports.repository.SportDisabilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisabilityService {

    private final DisabilityRepository disabilityRepository;
    private final SportDisabilityRepository sportDisabilityRepository;

    @Transactional
    public DisabilityResponse createDisability(DisabilityRequest request) {
        if (disabilityRepository.existsByName(request.getName())) {
            throw new RuntimeException("Ya existe una discapacidad con ese nombre");
        }

        Disability disability = new Disability();
        disability.setName(request.getName());
        disability.setDescription(request.getDescription());
        disability.setCategory(request.getCategory());
        disability.setIsActive(request.getIsActive());

        Disability savedDisability = disabilityRepository.save(disability);
        log.info("Discapacidad creada: {}", savedDisability.getName());

        return convertToResponse(savedDisability);
    }

    @Transactional(readOnly = true)
    public List<DisabilityResponse> getAllDisabilities() {
        return disabilityRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DisabilityResponse> getActiveDisabilities() {
        return disabilityRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DisabilityResponse getDisabilityById(Long id) {
        Disability disability = disabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discapacidad no encontrada con ID: " + id));
        return convertToResponse(disability);
    }

    @Transactional
    public DisabilityResponse updateDisability(Long id, DisabilityRequest request) {
        Disability disability = disabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discapacidad no encontrada con ID: " + id));

        if (request.getName() != null && !request.getName().equals(disability.getName())) {
            if (disabilityRepository.existsByName(request.getName())) {
                throw new RuntimeException("Ya existe una discapacidad con ese nombre");
            }
            disability.setName(request.getName());
        }

        if (request.getDescription() != null) {
            disability.setDescription(request.getDescription());
        }

        if (request.getCategory() != null) {
            disability.setCategory(request.getCategory());
        }

        if (request.getIsActive() != null) {
            disability.setIsActive(request.getIsActive());
        }

        Disability updatedDisability = disabilityRepository.save(disability);
        log.info("Discapacidad actualizada: {}", updatedDisability.getName());

        return convertToResponse(updatedDisability);
    }

    @Transactional
    public void deleteDisability(Long id) {
        Disability disability = disabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discapacidad no encontrada con ID: " + id));
        
        disabilityRepository.delete(disability);
        log.info("Discapacidad eliminada: {}", disability.getName());
    }

    private DisabilityResponse convertToResponse(Disability disability) {
        List<Long> sportIds = sportDisabilityRepository.findSportIdsByDisabilityId(disability.getId());
        
        return DisabilityResponse.builder()
                .id(disability.getId())
                .name(disability.getName())
                .description(disability.getDescription())
                .category(disability.getCategory())
                .isActive(disability.getIsActive())
                .sportIds(sportIds)
                .build();
    }
}