package com.example.notification.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Value ("${ms-auth.url.jslogin}")
    private String loginUrl;

    @Value ("${webSocketUrl}")
    private String webSocketUrl;

    @Value ("${staticResourceUrl}")
    private String staticResourceUrl;

    @RequestMapping("/")
    public String index (Model model) {
        model.addAttribute("loginUrl", loginUrl);
        model.addAttribute("webSocketUrl", webSocketUrl);
        model.addAttribute("staticUrl", staticResourceUrl);
        return "index";
    }
}
