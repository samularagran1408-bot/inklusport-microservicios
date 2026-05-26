package main.java.com.inklusport.controller;

import com.inklusport.auth.entity.UserPreferences;
import com.inklusport.auth.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
public class UserPreferencesController {

    @Autowired
    private UserPreferenceService userPreferenceService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserPreferences> obtenerPreferencias(@PathVariable String userId) {
        return ResponseEntity.ok(userPreferenceService.obtenerPorUsuario(userId));
    }

    @PostMapping
    public ResponseEntity<UserPreferences> guardarPreferencias(@RequestBody UserPreferences preferences) {
        return ResponseEntity.ok(userPreferenceService.guardarOActualizar(preferences));
    }
}