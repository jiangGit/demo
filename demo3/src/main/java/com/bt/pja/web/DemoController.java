package com.bt.pja.web;

import com.bt.pja.QueryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuchunjiang on 2017/5/11.
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Resource(name = "queryServiceImpl")
    private  QueryService queryService;

    @RequestMapping("/doboo")
    public ModelAndView test(HttpServletRequest request){

        return  new ModelAndView().addObject("hello",queryService.queryUserName(1));
    }

}
