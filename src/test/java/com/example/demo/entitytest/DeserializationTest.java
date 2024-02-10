//package com.example.demo.entitytest;
//
//
//import com.example.demo.entity.User;
//import com.example.demo.utility.SerializationTestHelper;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import junitparams.JUnitParamsRunner;
//import junitparams.Parameters;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(JUnitParamsRunner.class)
//public class DeserializationTest {
//
//    private static final SerializationTestHelper serdeser = SerializationTestHelper.newInstance();
//    private static final ObjectMapper mapper = new ObjectMapper();
//
//    public static Object[][] testDeserializationParams() {
//        return new Object[][] {
//                {User.class, "user.json"},
//
//        };
//    }
//
//    @Test
//    @Parameters(method = "testDeserializationParams")
//    public void testDeserialization(Class clazz, String resource) {
//        try {
//            String expected = serdeser.getResourceContent(resource);
//            String actual = serdeser.serialize(serdeser.deserializeResource(clazz, resource));
//            System.out.println("expected: "+expected   +"actual:"+actual);
//            Assert.assertEquals(mapper.readTree(expected), mapper.readTree(actual));
//
//        } catch (Exception e) {
//            Assert.fail("Unexpected error : " + e.getMessage());
//        }
//    }
//}
