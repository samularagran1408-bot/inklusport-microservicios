package com.inklusport.ia;

import com.inklusport.ai.repository.ChatFeedbackRepository;
import com.inklusport.ai.repository.ChatSessionRepository;
import com.inklusport.ai.service.GeminiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.inklusport.ai.AiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AiApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ChatFeedbackRepository chatFeedbackRepository;

    @MockBean
    private GeminiService geminiService;

    @BeforeEach
    void limpiarColecciones() {
        chatSessionRepository.deleteAll();
        chatFeedbackRepository.deleteAll();
        when(geminiService.getAIResponse(anyString(), anyList()))
                .thenReturn("Respuesta de prueba del asistente");
    }

    @Test
    void chatbot_devuelveRespuesta() throws Exception {
        String body = """
                {
                  "message": "hola, que deportes adaptados hay?",
                  "userId": "user-test-1"
                }
                """;

        mockMvc.perform(post("/api/ai/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Respuesta de prueba del asistente"))
                .andExpect(jsonPath("$.sessionId", notNullValue()));

        assertThat(chatSessionRepository.count()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void validacion_fallaSinMensaje() throws Exception {
        mockMvc.perform(post("/api/ai/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarFeedback() throws Exception {
        String body = """
                {
                  "mensajeId": "msg-test-456",
                  "util": true,
                  "comentario": "Muy util la respuesta",
                  "usuarioId": "user-test-1"
                }
                """;

        mockMvc.perform(post("/api/ai/feedback/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.util").value(true));
    }

    @Test
    void healthCheck_devuelveUP() throws Exception {
        mockMvc.perform(get("/api/ai/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
