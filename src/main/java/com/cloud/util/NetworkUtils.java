package com.cloud.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version v1.0
 * @author: TianXiang
 * @description:
 * @date: 2020/10/25
 */
public class NetworkUtils {

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 计算视频文件进度条的断点内容
     */
    public static void calculateVideoProgressBar(HttpServletRequest request, HttpServletResponse response, int videoByteSize) {
        String currentBreakpointIndex = request.getHeader("Range");//如果是video标签发起的请求就不会为null
        if(!StringUtils.isEmpty(currentBreakpointIndex)) {
            long breakpointStartIndex = Long.valueOf(currentBreakpointIndex.substring(currentBreakpointIndex.indexOf("=") + 1, currentBreakpointIndex.indexOf("-")));
            // 文件总字节数
            response.setContentLength(videoByteSize);
            //计算拖动的进度条断点, 且从断点开始继续播放
            response.setHeader("Content-Range", String.valueOf(-breakpointStartIndex + videoByteSize));
            response.setHeader("Accept-Ranges", "bytes");
        }
    }

}
