package com.inklusport.ai.util;

import org.springframework.stereotype.Component;

@Component
public class TextProcessor {

    public String cleanText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("\\s+", " ");
    }

    public boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }
}