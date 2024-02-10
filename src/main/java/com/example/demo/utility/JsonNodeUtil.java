//package com.example.demo.utility;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.IOException;
//import java.util.Optional;
//import java.util.TreeMap;
//
//public class JsonNodeUtil {
//
//    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//
//    public static Optional<String> getFieldAsOptionalString(JsonNode node, String field) {
//        return Optional.ofNullable(node.get(field)).map(JsonNode::asText);
//    }
//
//    public static Optional<Boolean> getFieldAsOptionalBoolean(JsonNode node, String field) {
//        return Optional.ofNullable(node.get(field)).map(JsonNode::asBoolean);
//    }
//
//    public static String getFieldAsString(JsonNode node, String field) {
//        return getFieldAsOptionalString(node, field).orElse(null);
//    }
//
//    public static Boolean getFieldAsBoolean(JsonNode node, String field) {
//        return getFieldAsOptionalBoolean(node, field).orElse(null);
//    }
//
//    public static Optional<String> getSubFieldAsOptionalString(JsonNode node, String... fields) {
//        JsonNode json = node.deepCopy();
//        for (int i = 0; i < fields.length - 1; i++) {
//            json = json.get(fields[i]);
//
//            if (json == null) {
//                return Optional.empty();
//            }
//        }
//
//        return getFieldAsOptionalString(json, fields[fields.length - 1]);
//    }
//
//    public static String getSubFieldAsString(JsonNode node, String... fields) {
//        return getSubFieldAsOptionalString(node, fields).orElse(null);
//    }
//
//    public static <T> Optional<T> parseSilently(String body, Class<T> clazz) {
//        return parse(body, clazz, true);
//    }
//
//    public static <T> Optional<T> parse(String body, Class<T> clazz) {
//        return parse(body, clazz, false);
//    }
//
//    public static <T> Optional<T> parse(String body, Class<T> clazz, boolean ignoreError) {
//        try {
//            return Optional.ofNullable(OBJECT_MAPPER.readValue(body, clazz));
//        } catch (IOException e) {
//            if (!ignoreError) {
//             //   Log.warn(e.getMessage());
//            }
//
//            return Optional.empty();
//        }
//    }
//
//    public static <T> Optional<T> parse(JsonNode body, Class<T> clazz) {
//        try {
//            return Optional.ofNullable(OBJECT_MAPPER.treeToValue(body, clazz));
//        } catch (Exception e) {
//         //   Log.warn(e.getMessage());
//            return Optional.empty();
//        }
//    }
//
//    public static Optional<JsonNode> getSubField(JsonNode node, String... fields) {
//        JsonNode json = node.deepCopy();
//        for (int i = 0; i < fields.length - 1; i++) {
//            json = json.get(fields[i]);
//
//            if (json == null) {
//                return Optional.empty();
//            }
//        }
//
//        return Optional.ofNullable(json.get(fields[fields.length - 1]));
//    }
//
//    public static Optional<JsonNode> parse(Object object) {
//        return Optional.ofNullable(OBJECT_MAPPER.valueToTree(object));
//    }
//
//    public static String serialize(Object object) throws JsonProcessingException {
//        return OBJECT_MAPPER.writeValueAsString(object);
//    }
//
//    /**
//     * Returns a TreeMap from a JSON object. The map is sorted by key names.
//     *
//     * @param object a JSON object (like a TransferRequest)
//     */
//    public static TreeMap<String, Object> getJsonAsSortedMap(Object object)
//        throws JsonProcessingException {
//        TreeMap<String, Object> sortedMap = null;
//        sortedMap =
//            OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(object), TreeMap.class);
//        return sortedMap;
//    }
//}
//