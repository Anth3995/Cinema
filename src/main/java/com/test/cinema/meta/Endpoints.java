package com.test.cinema.meta;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoints {
    private static final String API_PREFIX = "/api";
    private static final String API_VERSION = "/v1";
    private static final String API_V1 = API_PREFIX + API_VERSION;

    public static final String ORDERS = API_V1 + "/orders";
    public static final String MOVIES = API_V1 + "/movies";
}
