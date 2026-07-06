package com.inklusport.ai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comandos_voz")
public class VoiceCommand {
    @Id
    private String id;
    private String userId;
    private String transcript;
    private String commandType;
}
