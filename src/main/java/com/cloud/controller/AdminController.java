package com.cloud.controller;

import com.cloud.util.NetworkUtils;
import com.cloud.util.SysUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @version v1.0
 * @author: TianXiang
 * @description:
 * @date: 2020/10/21
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class.getName());

    /**
     * @param continueGetResource: newRequest/index (资源角标)
     */
    @GetMapping
    public void responseVideoData(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestParam("continueGetResource") String continueGetResource)throws Exception {
        LOG.info("收到点播请求的地址: {}, 持续获取资源的方式 {}",
                            NetworkUtils.getIpAddress(request),
                            "newRequest".equals(continueGetResource) ? "新请求" : "持续获取资源角标: "+continueGetResource);

        HttpSession session = request.getSession();
        List<String> filesPathList;
        if(SysUtils.isLinux()) {
            filesPathList = new ArrayList<String>() {{
                add("/usr/local/app/movies/破产姐妹第一季-01.mp4");
                add("/usr/local/app/movies/破产姐妹第一季-02.mp4");
                add("/usr/local/app/movies/破产姐妹第一季-03.mp4");
            }};
        }else {
            filesPathList = new ArrayList<String>() {{
                add("C:\\Users\\TX\\Desktop\\movies\\破产姐妹第一季-01.mp4");
                add("C:\\Users\\TX\\Desktop\\movies\\破产姐妹第一季-02.mp4");
                add("C:\\Users\\TX\\Desktop\\movies\\破产姐妹第一季-03.mp4");
            }};
        }

        if(!"newRequest".equals(continueGetResource)) {
            String path = filesPathList.get(Integer.valueOf(continueGetResource));
            filesPathList.clear();
            filesPathList.add(path);
        }
        int movieIndex = new Random().nextInt(filesPathList.size());
        String dibblingFilePath = filesPathList.get(movieIndex);
        File dibblingFile = new File(dibblingFilePath);
        session.setAttribute("movieName", dibblingFilePath.substring(dibblingFilePath.lastIndexOf(SysUtils.getSeparator()) + 1, dibblingFilePath.lastIndexOf(".")));
        session.setAttribute("movieIndex", movieIndex);
        FileInputStream inputStream = null;
        OutputStream outStream = null;
        try {
            inputStream = new FileInputStream(dibblingFile);
            NetworkUtils.calculateVideoProgressBar(request, response, inputStream.available());
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            outStream = response.getOutputStream();
            outStream.write(bytes);
            outStream.flush();
        } finally {
            if(null != inputStream) {
                inputStream.close();
            }
            if(null != outStream) {
                outStream.close();
            }
        }

    }

    @GetMapping("getMovieName")
    public Map<String, String> getMovieName(HttpServletRequest request) {
        Map map = new HashMap();
        map.put("movieName", request.getSession().getAttribute("movieName"));
        map.put("movieIndex", String.valueOf(request.getSession().getAttribute("movieIndex")));
        LOG.info("获取播放视频名称: " + map.get("movieName"));
        return map;
    }


}
