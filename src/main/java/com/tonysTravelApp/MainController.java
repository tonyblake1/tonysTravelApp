package com.tonysTravelApp;

import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


@Controller
public class MainController {
    
    @GetMapping("/")
    public String getIndexView() {
        return "index";
    }

    /*
     * Amadeus Market Insights API 
     * 
     * Key: IOip5CLp9eY8V03EM982GZefBchqOkW3
     * Secret: CFojIgO8SjMGTOGc
     */

    @RequestMapping("/getDestinations")
    public String getDestinations() {
        
        String url = "https://test.api.amadeus.com/v1/shopping/flight-destinations?origin=PAR&maxPrice=200";

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(url, String.class);

        /* ------------------------- Parse JSON data ---------------------------- */
        JsonParser jsonParser = JsonParserFactory.getJsonParser();

        Map<String, Object> map = jsonParser.parseMap(response);

        String mapArray[] = new String[map.size()];

        System.out.println("Items found: " + mapArray.length);

        int i = 0;

        for(Map.Entry<String, Object> entry: map.entrySet())
        {
            System.out.println(entry.getKey() + " - " + entry.getValue());

            i++;
        }
        
        return "Donezo";
    }
    
}
