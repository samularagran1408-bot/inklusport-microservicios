package com.inklusport.ia.service.impl;

import com.inklusport.ia.document.AnalisisBiomecanicoDocument;
import com.inklusport.ia.dto.request.AnalisisBiomecanicoRequest;
import com.inklusport.ia.dto.response.AnalisisBiomecanicoResponse;
import com.inklusport.ia.exception.IAResourceNotFoundException;
import com.inklusport.ia.repository.AnalisisBiomecanicoRepository;
import com.inklusport.ia.service.AnalisisBiomecanicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalisisBiomecanicoServiceImpl implements AnalisisBiomecanicoService {

    private final AnalisisBiomecanicoRepository analisisBiomecanicoRepository;

    @Override
    public AnalisisBiomecanicoResponse registrarAnalisis(AnalisisBiomecanicoRequest request) {
        double puntaje = calcularPuntaje(
                request.getRangoMovimiento(),
                request.getSimetria(),
                request.getEstabilidad()
        );
        List<String> recomendaciones = generarRecomendaciones(puntaje);

        AnalisisBiomecanicoDocument document = new AnalisisBiomecanicoDocument();
        document.setUsuarioId(request.getUsuarioId());
        document.setTipoDiscapacidad(request.getTipoDiscapacidad());
        document.setRangoMovimiento(request.getRangoMovimiento());
        document.setSimetria(request.getSimetria());
        document.setEstabilidad(request.getEstabilidad());
        document.setPuntaje(puntaje);
        document.setRecomendaciones(recomendaciones);
        document.setFechaAnalisis(Instant.now());

        AnalisisBiomecanicoDocument saved = analisisBiomecanicoRepository.save(document);
        return toResponse(saved);
    }

    @Override
    public List<AnalisisBiomecanicoResponse> consultarHistorialPorUsuario(String usuarioId) {
        List<AnalisisBiomecanicoResponse> historial = analisisBiomecanicoRepository.findAll().stream()
                .filter(a -> usuarioId.equals(a.getUsuarioId()))
                .sorted(Comparator.comparing(AnalisisBiomecanicoDocument::getFechaAnalisis).reversed())
                .map(this::toResponse)
                .collect(Collectors.toList());

        if (historial.isEmpty()) {
            throw new IAResourceNotFoundException("No se encontro historial biomecanico para el usuario: " + usuarioId);
        }
        return historial;
    }

    private double calcularPuntaje(int rangoMovimiento, int simetria, int estabilidad) {
        return (rangoMovimiento * 0.4) + (simetria * 0.3) + (estabilidad * 0.3);
    }

    private List<String> generarRecomendaciones(double puntaje) {
        List<String> recomendaciones = new ArrayList<>();
        if (puntaje < 40) {
            recomendaciones.add("Priorizar ejercicios de movilidad asistida y bajo impacto.");
            recomendaciones.add("Aumentar seguimiento clinico semanal para prevenir lesiones.");
            recomendaciones.add("Incluir entrenamiento de estabilidad basal con apoyo visual y de voz.");
        } else if (puntaje < 70) {
            recomendaciones.add("Mantener rutina progresiva con control de carga por sesion.");
            recomendaciones.add("Fortalecer patrones de simetria con ejercicios unilaterales adaptados.");
            recomendaciones.add("Agregar pausas activas para consolidar estabilidad postural.");
        } else {
            recomendaciones.add("Incrementar complejidad tecnica de forma gradual.");
            recomendaciones.add("Integrar ejercicios funcionales con desafios coordinativos.");
            recomendaciones.add("Monitorear fatiga para sostener rendimiento y prevenir sobrecarga.");
        }
        return recomendaciones;
    }

    private AnalisisBiomecanicoResponse toResponse(AnalisisBiomecanicoDocument document) {
        return AnalisisBiomecanicoResponse.builder()
                .id(document.getId())
                .usuarioId(document.getUsuarioId())
                .tipoDiscapacidad(document.getTipoDiscapacidad())
                .rangoMovimiento(document.getRangoMovimiento())
                .simetria(document.getSimetria())
                .estabilidad(document.getEstabilidad())
                .puntaje(document.getPuntaje())
                .recomendaciones(document.getRecomendaciones())
                .fechaAnalisis(document.getFechaAnalisis())
                .build();
    }
}
