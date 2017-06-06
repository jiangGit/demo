package com.bt.pja.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
