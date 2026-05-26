package main.java.com.inklusport.service;

import com.inklusport.auth.entity.UserPreferences;
import com.inklusport.auth.repository.UserPreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class UserPreferenceService {

    @Autowired
    private UserPreferencesRepository preferencesRepository;

    public UserPreferences obtenerPorUsuario(String userId) {
        return preferencesRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preferencias no encontradas para el usuario: " + userId));
    }

    public UserPreferences guardarOActualizar(UserPreferences nuevasPreferencias) {
        if (nuevasPreferencias.getId() == null) {
            nuevasPreferencias.setCreatedAt(Instant.now());
        }
        nuevasPreferencias.setUpdatedAt(Instant.now());
        return preferencesRepository.save(nuevasPreferencias);
    }
}