package com.example.client.controller;

import com.example.client.dto.Req;
import com.example.client.dto.UserResponse;
import com.example.client.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ApiController {

    private final RestTemplateService restTemplateService;

    public ApiController(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @GetMapping("/hello")
    public UserResponse getHello(){
        return restTemplateService.hello();
    }

    @PostMapping("/hello")
    public UserResponse postHello(){
        restTemplateService.post();
        return new UserResponse();
        //return restTemplateService.post();
    }

    @PostMapping("/exchange")
    public UserResponse exchange(){
        restTemplateService.exchange();
        return new UserResponse();
    }

    @PostMapping("/genericExchange")
    public Req<UserResponse> genericExchange(){
        return  restTemplateService.genericExchange();
    }
}
