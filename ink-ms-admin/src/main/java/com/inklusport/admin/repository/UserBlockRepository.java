package com.inklusport.admin.repository;

import com.inklusport.admin.entity.UserBlock;
import com.inklusport.admin.enums.BlockType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, String> {
    
    Optional<UserBlock> findByUserIdAndIsActiveTrue(String userId);
    
    Page<UserBlock> findByIsActiveTrue(Pageable pageable);
    
    Page<UserBlock> findByBlockTypeAndIsActiveTrue(BlockType blockType, Pageable pageable);
    
    /**
     * Busca bloqueos temporales que han expirado
     * Modificando el query devuelve una lista de objetos con el ID del bloqueo en la BD
     * y el ID del usuario bloqueado en el campo userId
     */
    @Query("SELECT b FROM UserBlock b WHERE b.isActive = true AND b.blockType = 'temporal' AND b.expiresAt < CURRENT_TIMESTAMP")
    List<UserBlock> findExpiredBlocks();
    
    /**
     * Desactiva un bloqueo específico, marcándolo como inactivo y registrando la fecha de desbloqueo
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserBlock b SET b.isActive = false, b.unblockedAt = CURRENT_TIMESTAMP WHERE b.id = :id")
    void deactivateBlock(@Param("id") String id);
    
    /**
     * Desactiva todos los bloqueos de un usuario, marcándolos como inactivos y registrando la fecha de desbloqueo
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserBlock b SET b.isActive = false WHERE b.userId = :userId AND b.isActive = true")
    void deactivateAllUserBlocks(@Param("userId") String userId);
}