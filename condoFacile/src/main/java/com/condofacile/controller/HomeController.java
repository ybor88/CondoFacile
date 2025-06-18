package com.condofacile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/Condofacile")
    public String home() {
        return "home"; // Rito-rna home.html da templates
    }

}