package com.example.demo.exception;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class Constants {

    public static final String BUGSNAG_PARTNER_KEY_NAME = "partner";
    public static final String BUGSNAG_PARTNER_PAYOUT_METHOD = "payout_method";
    public static final String BUGSNAG_PARTNER_OPERATION_REFERENCE = "operation_reference";
    public static final String BUGSNAG_PARTNER_ID_KEY_NAME = "partnerID";

    public static final String HTTP_500_MESSAGE =
        "The server sent HTTP status code 500: Internal Server Error";
    public static final String HTTP_504_MESSAGE =
        "The server sent HTTP status code 504: Gateway Timeout Error";

    public static final String HTTP_400_ERROR_CODE = "400";
    public static final String HTTP_401_ERROR_CODE = "401";
    public static final String HTTP_403_ERROR_CODE = "403";
    public static final String HTTP_404_ERROR_CODE = "404";
    public static final String HTTP_502_ERROR_CODE = "502";
    public static final String HTTP_503_ERROR_CODE = "503";
    public static final String HTTP_504_ERROR_CODE = "504";
    public static final String DEFAULT_HTTP_STATUS_CODE_PATH = "/httpCode";

    public static final String SOAP_ACTION_HEADER = "SOAPAction";
    public static final String SOAP_REQUEST_CONTENT_TYPE = "text/xml;charset=UTF-8";

    public static final int DAVIPLATA = 12010;

    public static final Map<Integer, String> HTTP_STATUS_MESSAGE_MAPPING =
        ImmutableMap.<Integer, String>builder()
            .put(500, HTTP_500_MESSAGE)
            .put(504, HTTP_504_MESSAGE)
            .build();

    public static final List<Integer> RESILIENT_HTTP_STATUS =
        ImmutableList.<Integer>builder().add(401, 403, 404, 500, 502, 503, 504).build();

    public static final String PARTNER_RESPONSE_CODE_HASH = "codeHash";
}
