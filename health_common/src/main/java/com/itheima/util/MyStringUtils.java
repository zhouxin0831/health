package com.itheima.util;

public class MyStringUtils {

    /**
     * 当value为空，或value为""，或value为"     "
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        if(value==null || value.length()==0 ||value.trim().length()==0){
            return true;
        }
        return false;
    }

    /**
     * 当value不为空时，且value不为""，且value不为"     "
     * @param value
     * @return
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);

    }
}
