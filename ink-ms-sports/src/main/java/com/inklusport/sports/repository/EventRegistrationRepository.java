package com.inklusport.sports.repository;

import com.inklusport.sports.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, String> {
    
    List<EventRegistration> findByUserId(String userId);
    
    List<EventRegistration> findByEventId(String eventId);
    
    Optional<EventRegistration> findByUserIdAndEventId(String userId, String eventId);
    
    boolean existsByEventIdAndUserId(String eventId, String userId);
    
    /**
     * Cuenta cuántos usuarios están en lista de espera (donde waitlist_position NO es nulo)
     */
    long countByEventIdAndWaitlistPositionIsNotNull(String eventId);

    /**
     * Busca al primero de la fila: el que tenga la posición más baja (ej: posición 1)
     */
    Optional<EventRegistration> findFirstByEventIdAndWaitlistPositionIsNotNullOrderByWaitlistPositionAsc(String eventId);

    /**
     * Trae a todos los que están en la fila para poder reordenarlos cuando uno se salga
     */
    List<EventRegistration> findByEventIdAndWaitlistPositionIsNotNullOrderByWaitlistPositionAsc(String eventId);
}