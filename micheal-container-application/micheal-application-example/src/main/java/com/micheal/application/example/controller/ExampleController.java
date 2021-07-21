package com.micheal.application.example.controller;

import com.micheal.application.example.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/7/20 14:05
 * @Description
 */
@RestController
@RequestMapping("/example")
public class ExampleController {

    @Autowired
    private ExampleService exampleService;

    @RequestMapping("/order")
    public String order() {
        return exampleService.order();
    }
}
