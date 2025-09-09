package com.tonysTravelApp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class TestController {

   List<String> cityList;

   @ModelAttribute
   public void preLoad(Model model){
  
      cityList = new ArrayList<>();
      cityList.add("Dublin");
      cityList.add("London");
      cityList.add("New York");
   }
    
   @GetMapping("/")
   public String getIndexView(Model model) {

      model.addAttribute("cityList", cityList);

      return "index";
   }

   @PostMapping(value = "/search")
   public String save(Model model) {
      
      return "searchResults";
   }
}
