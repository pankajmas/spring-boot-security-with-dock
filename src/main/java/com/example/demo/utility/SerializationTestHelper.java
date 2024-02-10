//package com.example.demo.utility;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.google.common.base.Charsets;
//import com.google.common.io.Resources;
//
//
//import java.io.IOException;
//
//public class SerializationTestHelper {
//    private ObjectMapper mapper;
//
//    public SerializationTestHelper(ObjectMapper mapper) {
//        this.mapper = mapper;
//        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
//    }
//
//    public static SerializationTestHelper newInstance() {
//        return new SerializationTestHelper(ObjectMapperFactory.objectMapperInstance());
//    }
////
////    public JsonNode getResourceAsJsonNode(String resourceName) {
////        try {
////            String resource =
////                Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
////            return mapper.readTree(resource);
////        } catch (IOException e) {
////            Assert.fail(e.getMessage());
////        }
////        return null;
////    }
//
//    public <T> T deserializeResource(Class<T> clazz, String resourceName) {
//        try {
//            String resource =
//                Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
//            return mapper.readValue(resource, clazz);
//        } catch (IOException e) {
//           // Assert.fail(e.getMessage());
//        }
//        return null;
//    }
//
////    public <T> T deserializeResource(TypeReference<T> typeReference, String resourceName) {
////        try {
////            String resource =
////                Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
////            return mapper.readValue(resource, typeReference);
////        } catch (IOException e) {
////            Assert.fail(e.getMessage());
////        }
////        return null;
////    }
//
////    public <T> T deserializeResource(
////        String resourceName, Class<T> genericClass, Class... genericTypes) {
////        try {
////            String resource =
////                Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
////            return mapper.readValue(
////                resource,
////                mapper.getTypeFactory().constructParametricType(genericClass, genericTypes));
////        } catch (IOException e) {
////            Assert.fail(e.getMessage());
////        }
////        return null;
////    }
////
////    public <T> T deserialize(Class<T> clazz, String json) {
////        try {
////            return mapper.readValue(json, clazz);
////        } catch (IOException e) {
////            Assert.fail(e.getMessage());
////        }
////        return null;
////    }
//
//    public String getResourceContent(String resourceName) {
//        try {
//            return Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
//        } catch (IOException e) {
//            //Assert.fail(e.getMessage());
//        }
//        return null;
//    }
//
//    public <T> String serialize(T object) {
//        try {
//            return mapper.writeValueAsString(object);
//        } catch (JsonProcessingException e) {
//           // Assert.fail(e.getMessage());
//        }
//        return null;
//    }
////
////    public <T> JsonNode serializeToJsonNode(T object) {
////        try {
////            String jsonStr = serialize(object);
////            return mapper.readTree(jsonStr);
////        } catch (IOException e) {
////            Assert.fail(e.getMessage());
////        }
////        return null;
////    }
////
////    public <T> String serializeToXMLString(T object) throws IOException {
////        try {
////            return mapper.writeValueAsString(object);
////        } catch (IOException e) {
////            Assert.fail(e.getMessage());
////        }
////        return null;
////    }
//}
