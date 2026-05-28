package main.java.com.inklusport.controller; 

import com.inklusport.auth.entity.UserPreferences;
import com.inklusport.auth.service.UserPreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
public class UserPreferencesController {

    private final UserPreferenceService userPreferenceService;

    public UserPreferencesController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> obtenerPreferencias(@PathVariable String userId) {
        try {
            UserPreferences preferences = userPreferenceService.obtenerPorUsuario(userId);
            if (preferences == null) {
         
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Preferencias no encontradas para el usuario: " + userId);
            }
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
           
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al obtener las preferencias: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> guardarPreferencias(@RequestBody UserPreferences preferences) {
        try {
            UserPreferences guardado = userPreferenceService.guardarOActualizar(preferences);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al guardar las preferencias: " + e.getMessage());
        }
    }
}