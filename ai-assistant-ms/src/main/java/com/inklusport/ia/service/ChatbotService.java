package com.inklusport.ia.service;

import com.inklusport.ia.dto.request.ChatbotQueryRequest;
import com.inklusport.ia.dto.response.ChatbotQueryResponse;

public interface ChatbotService {
    ChatbotQueryResponse procesarMensaje(ChatbotQueryRequest request);
}
