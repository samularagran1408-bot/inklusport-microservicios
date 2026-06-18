package com.inklusport.ai.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheService {

    /**
     * Cache para respuestas de chat
     */
    private final Cache<String, String> chatResponseCache;
    
    /**
     * Cache para clasificación de intenciones
     */
    private final Cache<String, String> intentCache;
    
    /**
     * Cache para sesiones de chat
     */
    private final Cache<String, Object> sessionCache;

    public CacheService() {
        /**
         * Cache de respuestas - 1000 elementos, 1 hora de expiración
         */
        this.chatResponseCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .recordStats()
                .build();
        
        /**
         * Cache de intenciones - 500 elementos, 30 minutos de expiración
         */
        this.intentCache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats()
                .build();
        
        /**
         * Cache de sesiones - 200 elementos, 15 minutos de expiración
         */
        this.sessionCache = Caffeine.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .recordStats()
                .build();
        
        log.info("Servicio de caché inicializado");
    }

    /**
     * Obtener respuesta de caché
     */
    public String getCachedResponse(String key) {
        return chatResponseCache.getIfPresent(key);
    }

    /**
     * Guardar respuesta en caché
     */
    public void cacheResponse(String key, String response) {
        chatResponseCache.put(key, response);
    }

    /**
     * Obtener intención de caché
     */
    public String getCachedIntent(String key) {
        return intentCache.getIfPresent(key);
    }

    /**
     * Guardar intención en caché
     */
    public void cacheIntent(String key, String intent) {
        intentCache.put(key, intent);
    }

    /**
     * Obtener sesión de caché
     */
    public Object getCachedSession(String key) {
        return sessionCache.getIfPresent(key);
    }

    /**
     * Guardar sesión en caché
     */
    public void cacheSession(String key, Object session) {
        sessionCache.put(key, session);
    }

    /**
     * Limpiar caché
     */
    public void clearAllCaches() {
        chatResponseCache.invalidateAll();
        intentCache.invalidateAll();
        sessionCache.invalidateAll();
        log.info("🧹 Todos los cachés limpiados");
    }

    /**
     * Obtener estadísticas de caché
     */
    public Map<String, Object> getCacheStats() {
        return Map.of(
            "chatResponseCache", chatResponseCache.stats(),
            "intentCache", intentCache.stats(),
            "sessionCache", sessionCache.stats()
        );
    }
}