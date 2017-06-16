package com.bt.pja.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by liuchunjiang on 2017/5/19.
 */
@Controller
public class HomeController {
    @RequestMapping(value="/", method=GET)
    public String home() {

        System.out.println("home");
        return "home";
    }

    @RequestMapping(value="/header", method=GET)
    @ResponseBody
    public String header(HttpServletRequest request, HttpServletResponse response){
        StringBuilder sb = new StringBuilder();
        Enumeration<String> hs = request.getHeaderNames();
        String eTag = null;
        sb.append("<html>");
        while (hs.hasMoreElements()) {
            String string = (String) hs.nextElement();
            sb.append(String.format("%s:%s<br/>",string, request.getHeader(string)));
            if (string.equals("http_if-none-match")) {
                eTag = request.getHeader(string);
            }
            if (string.equals("if-none-match")) {
                eTag = request.getHeader(string);
            }
        }
        sb.append("</html>");

        return sb.toString();
    }


}
