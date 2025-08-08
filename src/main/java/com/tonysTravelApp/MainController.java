package com.tonysTravelApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String getIndexView() {
        return "index";
    }
    
}
