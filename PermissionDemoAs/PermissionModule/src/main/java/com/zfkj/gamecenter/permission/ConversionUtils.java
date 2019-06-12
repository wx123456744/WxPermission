package com.zfkj.gamecenter.permission;

/**
 * Created by win7 on 2019/5/28.
 *
 *  简单的类型处理
 */

public class ConversionUtils {
    public static boolean isEmpty(Object obj) {
        String str = toString(obj);
        if ("null".equals(str) || (str == null) ||(str.trim().length() <= 0) || (str.isEmpty())){
            return true;
        }
        return false;
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static int toInt(String obj) {
        if (isEmpty(obj)) {
            return 0;
        }
        return Integer.parseInt(obj.toString());
    }
}
