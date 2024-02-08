package com.example.demo.utility;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class ObjectMapperFactory {

    private static ObjectMapper objectMapper;


    public static ObjectMapper objectMapperInstance() {
        if (objectMapper == null) {
            objectMapper = newObjectMapper();
        }

        return objectMapper;
    }

//    public static XmlMapper xmlMapperInstance() {
//        if (xmlMapper == null) {
//            xmlMapper = newXmlMapper();
//        }
//
//        return xmlMapper;
//    }

    public static ObjectMapper newObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        setDefaultProps(objectMapper);
        return objectMapper;
    }

//    private static XmlMapper newXmlMapper() {
//        XmlMapper xmlMapper = new XmlMapper();
//        setDefaultProps(xmlMapper);
//       // xmlMapper.setAnnotationIntrospector(new DefaultNamespaceInstrospector());
//        return xmlMapper;
//    }

    private static void setDefaultProps(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(df);
        DateTimeFormatter ISO_LOCAL_DATE_TIME =
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral(' ')
                .append(DateTimeFormatter.ISO_TIME)
                .toFormatter();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(
            LocalDateTime.class, new LocalDateTimeDeserializer(ISO_LOCAL_DATE_TIME));
        javaTimeModule.addSerializer(
            LocalDateTime.class, new LocalDateTimeSerializer(ISO_LOCAL_DATE_TIME));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JodaModule());
    }
}
