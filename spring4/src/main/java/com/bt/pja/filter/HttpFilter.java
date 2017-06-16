package com.bt.pja.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by liuchunjiang on 2017/6/15.
 */
public class HttpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse reps = (HttpServletResponse)response;
        Enumeration<String> hs = req.getHeaderNames();
        String eTag = null;
        while (hs.hasMoreElements()) {
            String string = (String) hs.nextElement();
            if (string.equals("http_if-none-match")) {
                eTag = req.getHeader(string);
            }
            if (string.equals("if-none-match")) {
                eTag = req.getHeader(string);
            }

        }
        System.out.println(eTag);
        chain.doFilter(request,response);

    }

    @Override
    public void destroy() {

    }
}
