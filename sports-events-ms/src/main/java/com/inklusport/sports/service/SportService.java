package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.SportRequest;
import com.inklusport.sports.dto.response.SportResponse;
import com.inklusport.sports.entity.Sport;
import com.inklusport.sports.entity.SportDisability;
import com.inklusport.sports.repository.SportRepository;
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
public class SportService {

    private final SportRepository sportRepository;
    private final SportDisabilityRepository sportDisabilityRepository;

    @Transactional
    public SportResponse createSport(SportRequest request) {
        if (sportRepository.existsByName(request.getName())) {
            throw new RuntimeException("Ya existe un deporte con ese nombre");
        }

        Sport sport = new Sport();
        sport.setName(request.getName());
        sport.setDescription(request.getDescription());
        
        if (request.getDifficulty() != null) {
            sport.setDifficulty(Sport.Difficulty.valueOf(request.getDifficulty().toLowerCase()));
        }
        
        sport.setRequiredMaterials(request.getRequiredMaterials());
        sport.setIsActive(request.getIsActive());

        Sport savedSport = sportRepository.save(sport);
        log.info("Deporte creado: {}", savedSport.getName());

        return convertToResponse(savedSport);
    }

    @Transactional(readOnly = true)
    public List<SportResponse> getAllSports() {
        return sportRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SportResponse> getActiveSports() {
        return sportRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SportResponse> getSportsByDisability(Long disabilityId) {
        return sportRepository.findSportsByDisabilityId(disabilityId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SportResponse getSportById(Long id) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado con ID: " + id));
        return convertToResponse(sport);
    }

    @Transactional
    public SportResponse updateSport(Long id, SportRequest request) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado con ID: " + id));

        if (request.getName() != null && !request.getName().equals(sport.getName())) {
            if (sportRepository.existsByName(request.getName())) {
                throw new RuntimeException("Ya existe un deporte con ese nombre");
            }
            sport.setName(request.getName());
        }

        if (request.getDescription() != null) {
            sport.setDescription(request.getDescription());
        }

        if (request.getDifficulty() != null) {
            sport.setDifficulty(Sport.Difficulty.valueOf(request.getDifficulty().toLowerCase()));
        }

        if (request.getRequiredMaterials() != null) {
            sport.setRequiredMaterials(request.getRequiredMaterials());
        }

        if (request.getIsActive() != null) {
            sport.setIsActive(request.getIsActive());
        }

        Sport updatedSport = sportRepository.save(sport);
        log.info("Deporte actualizado: {}", updatedSport.getName());

        return convertToResponse(updatedSport);
    }

    @Transactional
    public void deleteSport(Long id) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado con ID: " + id));
        
        sportRepository.delete(sport);
        log.info("Deporte eliminado: {}", sport.getName());
    }

    @Transactional
    public void activateSport(Long id) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado con ID: " + id));
        
        sport.setIsActive(true);
        sportRepository.save(sport);
        log.info("Deporte activado: {}", sport.getName());
    }

    @Transactional
    public void deactivateSport(Long id) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado con ID: " + id));
        
        sport.setIsActive(false);
        sportRepository.save(sport);
        log.info("Deporte desactivado: {}", sport.getName());
    }

    private SportResponse convertToResponse(Sport sport) {
        List<Long> disabilityIds = sportDisabilityRepository.findDisabilityIdsBySportId(sport.getId());
        
        return SportResponse.builder()
                .id(sport.getId())
                .name(sport.getName())
                .description(sport.getDescription())
                .difficulty(sport.getDifficulty() != null ? sport.getDifficulty().toString() : null)
                .requiredMaterials(sport.getRequiredMaterials())
                .isActive(sport.getIsActive())
                .createdAt(sport.getCreatedAt())
                .disabilityIds(disabilityIds)
                .build();
    }
}