package com.bt.pja.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuchunjiang on 2017/6/16.
 */
@Controller
public class LoadImgController {
    private static Map<String, String> contentTypeMap = new HashMap<String, String>() ;
    static {
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("ico", "image/x-icon");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("gif", "image/gif");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("doc", "application/msword");
        contentTypeMap.put("ppt", "application/vnd.ms-powerpoint");
        contentTypeMap.put("avi", "video/avi");
        contentTypeMap.put("mp3", "audio/mp3");
        contentTypeMap.put("mp4", "video/mpeg4");
    }
    @RequestMapping(value = "/{fileName:\\S+}.{fix:png|jpg|gif|jpeg|ico}")
    public void loadPng(@PathVariable("fileName") String fileName,@PathVariable("fix") String fix, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Enumeration<String> hs = request.getHeaderNames();
        response.setContentType(contentTypeMap.get(fix));
        response.setCharacterEncoding("UTF-8");
        String etag = request.getHeader("if-none-match");
        if (etag!= null){
            System.out.println(request.getHeader("if-none-match"));
            response.setStatus(304);
            response.setHeader("ETag",etag);
            response.addHeader("Last-Modified", "Thu, 10 Sep 2015 13:18:07 GMT");
            response.addHeader("Expires", "Thu, 10 Sep 2025 13:18:07 GMT");
            return;
        }else {
            etag =  String.format("\"%s\"",new Date().getTime());
        }

        String path = request.getServletContext().getRealPath("/");
        File file = new File(path+"/"+fileName+"."+fix);
        if (!file.exists()){
            response.setStatus(404);
            return;
        }
        InputStream in = new FileInputStream(file);
        response.addHeader("ETag", etag);
        response.addHeader("Last-Modified", "Thu, 10 Sep 2015 13:18:07 GMT");
        response.addHeader("Expires", "Thu, 10 Sep 2025 13:18:07 GMT");
        OutputStream out = response.getOutputStream();
        if(in != null){
            byte[] buffer = new byte[1024];
            int n = 0;
            while((n = in.read(buffer)) > 0){
                out.write(buffer,0,n);
            }
            out.flush();
            out.close();
            in.close();
            response.flushBuffer();
        }
    }
}
