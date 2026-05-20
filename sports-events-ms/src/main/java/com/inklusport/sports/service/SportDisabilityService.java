package com.inklusport.sports.service;

import com.inklusport.sports.dto.request.SportDisabilityRequest;
import com.inklusport.sports.dto.response.SportDisabilityResponse;
import com.inklusport.sports.entity.Disability;
import com.inklusport.sports.entity.Sport;
import com.inklusport.sports.entity.SportDisability;
import com.inklusport.sports.entity.SportDisabilityId;
import com.inklusport.sports.repository.DisabilityRepository;
import com.inklusport.sports.repository.SportDisabilityRepository;
import com.inklusport.sports.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SportDisabilityService {

    private final SportDisabilityRepository sportDisabilityRepository;
    private final SportRepository sportRepository;
    private final DisabilityRepository disabilityRepository;

    @Transactional
    public SportDisabilityResponse addAssociation(SportDisabilityRequest request) {
        Sport sport = sportRepository.findById(request.getSportId())
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado con ID: " + request.getSportId()));

        Disability disability = disabilityRepository.findById(request.getDisabilityId())
                .orElseThrow(() -> new RuntimeException("Discapacidad no encontrada con ID: " + request.getDisabilityId()));

        if (sportDisabilityRepository.existsBySportIdAndDisabilityId(request.getSportId(), request.getDisabilityId())) {
            throw new RuntimeException("La asociación ya existe");
        }

        SportDisabilityId id = new SportDisabilityId(request.getSportId(), request.getDisabilityId());
        
        SportDisability sportDisability = new SportDisability();
        sportDisability.setId(id);
        sportDisability.setSport(sport);
        sportDisability.setDisability(disability);
        sportDisability.setAdaptations(request.getAdaptations());

        SportDisability saved = sportDisabilityRepository.save(sportDisability);
        log.info("Asociación creada: Deporte {} - Discapacidad {}", sport.getName(), disability.getName());

        return convertToResponse(saved);
    }

    @Transactional
    public void removeAssociation(Long sportId, Long disabilityId) {
        SportDisabilityId id = new SportDisabilityId(sportId, disabilityId);
        
        if (!sportDisabilityRepository.existsById(id)) {
            throw new RuntimeException("La asociación no existe");
        }
        
        sportDisabilityRepository.deleteById(id);
        log.info("Asociación eliminada: Deporte {} - Discapacidad {}", sportId, disabilityId);
    }

    @Transactional(readOnly = true)
    public List<SportDisabilityResponse> getAssociationsBySport(Long sportId) {
        return sportDisabilityRepository.findBySportId(sportId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SportDisabilityResponse> getAssociationsByDisability(Long disabilityId) {
        return sportDisabilityRepository.findByDisabilityId(disabilityId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String getAdaptations(Long sportId, Long disabilityId) {
        SportDisabilityId id = new SportDisabilityId(sportId, disabilityId);
        
        return sportDisabilityRepository.findById(id)
                .map(SportDisability::getAdaptations)
                .orElseThrow(() -> new RuntimeException("No se encontraron adaptaciones para esta combinación"));
    }

    private SportDisabilityResponse convertToResponse(SportDisability sd) {
        return SportDisabilityResponse.builder()
                .sportId(sd.getSport().getId())
                .sportName(sd.getSport().getName())
                .disabilityId(sd.getDisability().getId())
                .disabilityName(sd.getDisability().getName())
                .adaptations(sd.getAdaptations())
                .build();
    }
}