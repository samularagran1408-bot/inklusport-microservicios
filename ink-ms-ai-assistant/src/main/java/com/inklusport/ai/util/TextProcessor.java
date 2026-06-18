package com.inklusport.ai.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TextProcessor {

    public List<String> extractKeywords(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return Arrays.stream(text.toLowerCase().split("\\s+"))
                .map(word -> word.replaceAll("[^a-záéíóúñ0-9]", ""))
                .filter(word -> word.length() > 2)
                .distinct()
                .collect(Collectors.toList());
    }
}
