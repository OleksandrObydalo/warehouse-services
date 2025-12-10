package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

public class JsonValidator {

    public static void main(String[] args) {
        String schemaPath = "json/warehouse_schema.json";
        String dataPath = "json/warehouse_data.json";

        try {
            boolean isValid = validateJson(schemaPath, dataPath);
            if (isValid) {
                System.out.println("Успіх! JSON документ повністю відповідає схемі.");
            } else {
                System.out.println("Помилка! JSON документ невалідний.");
            }
        } catch (IOException e) {
            System.err.println("Помилка читання файлів: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean validateJson(String schemaPath, String dataPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        JsonSchema schema = factory.getSchema(new FileInputStream(schemaPath));
        JsonNode jsonNode = mapper.readTree(new FileInputStream(dataPath));

        Set<ValidationMessage> errors = schema.validate(jsonNode);
        if (!errors.isEmpty()) {
            System.out.println("Знайдено помилок: " + errors.size());
            for (ValidationMessage error : errors) {
                System.out.println(" -> " + error.getMessage());
            }
            return false;
        }

        return true;
    }
}
