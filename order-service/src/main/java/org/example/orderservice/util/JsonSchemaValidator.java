package org.example.orderservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class JsonSchemaValidator {

    private static final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void validatePlaceServiceDTO(Object dto, String schemaPath) throws ValidationException, IOException {
        try {
            ClassPathResource resource = new ClassPathResource(schemaPath);
            InputStream schemaStream = resource.getInputStream();
            JsonSchema schema = factory.getSchema(schemaStream);

            String json = objectMapper.writeValueAsString(dto);
            Set<ValidationMessage> errors = schema.validate(objectMapper.readTree(json));

            if (!errors.isEmpty()) {
                StringBuilder errorMsg = new StringBuilder("Validation failed: ");
                errors.forEach(error -> errorMsg.append(error.getMessage()).append("; "));
                throw new ValidationException(errorMsg.toString());
            }
        } catch (Exception e) {
            if (e instanceof ValidationException) {
                throw e;
            }
            throw new ValidationException("Schema validation error: " + e.getMessage());
        }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}

