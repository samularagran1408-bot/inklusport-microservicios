package com.inklusport.ai;

import com.inklusport.ai.repository.BiomechanicalAnalysisRepository;
import com.inklusport.ai.repository.ChatSessionRepository;
import com.inklusport.ai.repository.TrainingPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AiApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BiomechanicalAnalysisRepository biomechanicalAnalysisRepository;

    @Autowired
    private TrainingPlanRepository trainingPlanRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @BeforeEach
    void limpiarColecciones() {
        biomechanicalAnalysisRepository.deleteAll();
        trainingPlanRepository.deleteAll();
        chatSessionRepository.deleteAll();
    }

    @Test
    void registrarAnalisis_calculaPuntajeYRecomendaciones() throws Exception {
        String body = """
                {
                  "userId": "user-test-1",
                  "ejercicioNombre": "Sentadilla",
                  "disabilityType": "motriz",
                  "movementData": {
                    "rangeOfMotion": 65,
                    "symmetry": 70,
                    "stability": 60
                  }
                }
                """;

        mockMvc.perform(post("/api/ai/biomechanical/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.generalScore").exists())
                .andExpect(jsonPath("$.fatigueLevel").exists())
                .andExpect(jsonPath("$.recommendations").exists());

        assertThat(biomechanicalAnalysisRepository.count()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void historialUsuario_devuelveLista() throws Exception {
        // Primero registrar un análisis
        String body = """
                {
                  "userId": "user-hist",
                  "ejercicioNombre": "Flexión",
                  "movementData": {
                    "rangeOfMotion": 50,
                    "symmetry": 50,
                    "stability": 50
                  }
                }
                """;

        mockMvc.perform(post("/api/ai/biomechanical/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // Luego consultar historial
        mockMvc.perform(get("/api/ai/biomechanical/user/user-hist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void chatbot_devuelveRespuestaPorPalabrasClave() throws Exception {
        String body = """
                {
                  "message": "hola necesito ayuda"
                }
                """;

        mockMvc.perform(post("/api/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").exists())
                .andExpect(jsonPath("$.sessionId").exists());

        assertThat(chatSessionRepository.count()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void chatbot_conversacionContinua() throws Exception {
        String body1 = """
                {
                  "message": "que eventos hay"
                }
                """;

        String response = mockMvc.perform(post("/api/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String sessionId = "test-session-id";

        String body2 = """
                {
                  "message": "como me inscribo",
                  "sessionId": "test-session-id"
                }
                """;

        mockMvc.perform(post("/api/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").exists());

        assertThat(chatSessionRepository.count()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void generarPlanEntrenamiento() throws Exception {
        String body = """
                {
                  "sport": "natación",
                  "disabilityType": "fisica",
                  "level": "intermedio",
                  "durationWeeks": 4
                }
                """;

        mockMvc.perform(post("/api/ai/training/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planId").exists())
                .andExpect(jsonPath("$.exercises").exists());

        assertThat(trainingPlanRepository.count()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void validacion_fallaSinCamposObligatorios() throws Exception {
        mockMvc.perform(post("/api/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registrarFeedback() throws Exception {
        String body = """
                {
                  "conversacionId": "conv-test-123",
                  "mensajeId": "msg-test-456",
                  "util": true,
                  "comentario": "Muy útil la respuesta"
                }
                """;

        mockMvc.perform(post("/api/ai/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.util").value(true));
    }

    @Test
    void healthCheck_devuelveUP() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}