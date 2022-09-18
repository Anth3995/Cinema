package com.test.cinema.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class SearchUtils {
    public static String unifySearchParamSafe(String value) {
        return Objects.toString(value, "").toLowerCase();
    }
}
