package com.huang.framework.controller;

import com.huang.framework.annotation.TRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author -Huang
 * @create 2020-03-15 17:47
 */
@TRestController
public class TestController {


    @GetMapping("/test")
    public String test(){
        return "testtttt";
    }

    @GetMapping("/code")
    public String getCode(@RequestParam String mobile){
        return mobile;
    }
}
