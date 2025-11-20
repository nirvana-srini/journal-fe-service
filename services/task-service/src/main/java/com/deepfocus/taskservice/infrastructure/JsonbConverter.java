package com.deepfocus.taskservice.infrastructure;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Converter
public class JsonbConverter implements AttributeConverter<String, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            // If it's already valid JSON, return as is
            if (attribute == null || attribute.trim().isEmpty()) {
                return null;
            }
            // Try to parse to validate it's valid JSON
            objectMapper.readTree(attribute);
            return attribute;
        } catch (JsonProcessingException e) {
            // If not valid JSON, wrap it as a JSON string
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException("Error converting JSON attribute to database column", ex);
            }
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData; // Return as is, it's already a String
    }
}
