package com.team4.ttukttak_parking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ttukttak-parking")
public class FrontController {

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

}
