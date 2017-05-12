package com.bt.pja.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuchunjiang on 2017/5/5.
 */

@Controller
@RequestMapping(value = "/demo")
public class DemoCotroller {

    @RequestMapping(value = "/test")
    public ModelAndView test(HttpServletRequest request){
        for (int i=0;i<100;i++){
            System.out.println(i);
        }
        return  new ModelAndView().addObject("hello","world");
    }
}
