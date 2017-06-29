package com.bt.pja.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by liuchunjiang on 2017/5/19.
 */


@Configuration
@ComponentScan(basePackages={"com.bt.pja"},
        excludeFilters={
                @Filter(type=FilterType.ANNOTATION, value=EnableWebMvc.class)
        })
public class RootConfig {
}
