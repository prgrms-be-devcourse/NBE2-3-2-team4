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





    @GetMapping("/buy-main")
    public String parkBuyMainPage() {
        return "parkbuymain";
    }


    @GetMapping("/member-park")
    public String parkMember() {
        return "memberpark";
    }

    @GetMapping("/member-park-record")
    public String parkMemberRecord() {
        return "memberparkrecord";
    }


    @GetMapping("/member-park-record-detail")
    public String parkMemberRecordDetail() {
        return "memberparkrecorddetail";
    }

}
