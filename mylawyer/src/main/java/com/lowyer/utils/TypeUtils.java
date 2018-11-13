package com.lowyer.utils;

import com.alibaba.fastjson.JSONException;

public class TypeUtils {
	
	public static final Boolean castToBoolean(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }

        if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0) {
                return null;
            }

            if ("true".equals(str)) {
                return Boolean.TRUE;
            }
            if ("false".equals(str)) {
                return Boolean.FALSE;
            }

            if ("1".equals(str)) {
                return Boolean.TRUE;
            }
            
            // 字符串转布尔补丁
            if ("0".equals(str)) {
                return Boolean.FALSE;
            }

            if ("Y".equalsIgnoreCase(str)) {
                return Boolean.TRUE;
            }
            if ("N".equalsIgnoreCase(str)) {
                return Boolean.FALSE;
            }
            
            if ("T".equalsIgnoreCase(str)) {
                return Boolean.TRUE;
            }
            if ("F".equalsIgnoreCase(str)) {
                return Boolean.FALSE;
            }
        }

        throw new JSONException("can not cast to int, value : " + value);
    }

}
