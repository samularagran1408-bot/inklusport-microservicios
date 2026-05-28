package com.inklusport.ia.service;

import com.inklusport.ia.dto.request.AnalisisBiomecanicoRequest;
import com.inklusport.ia.dto.response.AnalisisBiomecanicoResponse;

import java.util.List;

public interface AnalisisBiomecanicoService {
    AnalisisBiomecanicoResponse registrarAnalisis(AnalisisBiomecanicoRequest request);

    List<AnalisisBiomecanicoResponse> consultarHistorialPorUsuario(String usuarioId);
}
