package com.cloud.util;

import java.util.Properties;

/**
 * @version v1.0
 * @author: TianXiang
 * @description:
 * @date: 2020/10/25
 */
public class SysUtils {

    /**
     * 当前系统为Linux
     */
    public static boolean isLinux() {
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得当前系统路径分隔符
     */
    public static String getSeparator() {
       return System.getProperty("file.separator");
    }

}
