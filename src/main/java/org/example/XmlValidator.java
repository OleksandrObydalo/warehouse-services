package org.example;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;

public class XmlValidator {

    public static void main(String[] args) {
        String xsdPath = "xml/warehouse_schema.xsd";
        String xmlPath = "xml/warehouse_data.xml";

        boolean isValid = validateXMLSchema(xsdPath, xmlPath);

        if (isValid) {
            System.out.println("Успіх! XML файл є валідним і відповідає XSD схемі.");
        } else {
            System.out.println("Помилка! XML файл не відповідає схемі.");
        }
    }

    public static boolean validateXMLSchema(String xsdPath, String xmlPath) {
        try {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));

            return true;

        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
            return false;
        } catch (SAXException e) {
            System.out.println("Помилка валідації:");
            System.out.println(" -> " + e.getMessage());
            return false;
        }
    }
}
