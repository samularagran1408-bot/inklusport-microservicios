package com.inklusport.ia;

import com.inklusport.ia.repository.AnalisisBiomecanicoRepository;
import com.inklusport.ia.repository.ConversacionChatbotRepository;
import com.inklusport.ia.repository.PlanEntrenamientoRepository;
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
class IaApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalisisBiomecanicoRepository analisisBiomecanicoRepository;

    @Autowired
    private PlanEntrenamientoRepository planEntrenamientoRepository;

    @Autowired
    private ConversacionChatbotRepository conversacionChatbotRepository;

    @BeforeEach
    void limpiarColecciones() {
        analisisBiomecanicoRepository.deleteAll();
        planEntrenamientoRepository.deleteAll();
        conversacionChatbotRepository.deleteAll();
    }

    @Test
    void registrarAnalisis_calculaPuntajeYRecomendaciones() throws Exception {
        String body = """
                {
                  "usuarioId": "user-test-1",
                  "tipoDiscapacidad": "MOTORA",
                  "rangoMovimiento": 65,
                  "simetria": 70,
                  "estabilidad": 60
                }
                """;

        mockMvc.perform(post("/api/ia/analisis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.puntaje").value(65.0))
                .andExpect(jsonPath("$.recomendaciones", hasSize(3)))
                .andExpect(jsonPath("$.usuarioId").value("user-test-1"));

        assertThat(analisisBiomecanicoRepository.count()).isEqualTo(1);
    }

    @Test
    void historialUsuario_devuelveLista() throws Exception {
        registrarAnalisisPara("user-hist");

        mockMvc.perform(get("/api/ia/analisis/usuario/user-hist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].usuarioId").value("user-hist"));
    }

    @Test
    void historialUsuario_sinDatos_devuelve404() throws Exception {
        mockMvc.perform(get("/api/ia/analisis/usuario/no-existe"))
                .andExpect(status().isNotFound());
    }

    @Test
    void planEntrenamiento_creaYActualiza() throws Exception {
        String body = """
                {
                  "usuarioId": "user-plan",
                  "entrenadorId": "coach-1",
                  "ejercicios": [
                    {
                      "nombreEjercicio": "Sentadilla asistida",
                      "adaptaciones": ["voz", "visual"]
                    }
                  ]
                }
                """;

        mockMvc.perform(put("/api/ia/planes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ejercicios", hasSize(1)))
                .andExpect(jsonPath("$.id", notNullValue()));

        mockMvc.perform(put("/api/ia/planes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.replace("Sentadilla asistida", "Plancha adaptada")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ejercicios[0].nombreEjercicio").value("Plancha adaptada"));

        assertThat(planEntrenamientoRepository.count()).isEqualTo(1);
    }

    @Test
    void chatbot_devuelveRespuestaFijaPorIntencion() throws Exception {
        String body = """
                {
                  "usuarioId": "user-chat",
                  "mensaje": "hola necesito ayuda"
                }
                """;

        mockMvc.perform(post("/api/ia/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intencionDetectada").value("SALUDO"))
                .andExpect(jsonPath("$.respuestaBot", notNullValue()))
                .andExpect(jsonPath("$.estadoConversacion").value("ACTIVA"));

        assertThat(conversacionChatbotRepository.count()).isEqualTo(1);
    }

    @Test
    void chatbot_cierreMarcaConversacionCerrada() throws Exception {
        String body = """
                {
                  "usuarioId": "user-chat-2",
                  "mensaje": "gracias adios"
                }
                """;

        mockMvc.perform(post("/api/ia/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intencionDetectada").value("CIERRE"))
                .andExpect(jsonPath("$.estadoConversacion").value("CERRADA"));
    }

    @Test
    void validacion_fallaSinCamposObligatorios() throws Exception {
        mockMvc.perform(post("/api/ia/analisis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    private void registrarAnalisisPara(String usuarioId) throws Exception {
        String body = """
                {
                  "usuarioId": "%s",
                  "tipoDiscapacidad": "MOTORA",
                  "rangoMovimiento": 50,
                  "simetria": 50,
                  "estabilidad": 50
                }
                """.formatted(usuarioId);

        mockMvc.perform(post("/api/ia/analisis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }
}
