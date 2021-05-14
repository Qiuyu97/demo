package com.qiuyu.demo.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description:
 * @Author: qiuyu
 * @Date: 2021/4/7
 **/
@Controller
public class WebSocketController {

    @GetMapping("/index")
    public String re(){
        return "sharedOrder";
    }

}
