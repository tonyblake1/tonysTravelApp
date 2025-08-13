package com.tonysTravelApp;

import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
* FlightSearchController handles flight search requests using Amadeus API based on user input.
*/
@RestController
public class FlightSearchController {
   
   private final AmadeusApiClientService amadeusApiClientService;
   
   public FlightSearchController(AmadeusApiClientService amadeusApiClientService) {
       this.amadeusApiClientService = amadeusApiClientService;
   }
   /**
    * Search for flights using the Amadeus API.
    * @param origin The origin airport code (e.g., "LAX").
    * @param destination The destination airport code (e.g., "JFK").
    * @param departureDate The departure date in the format "YYYY-MM-DD".
    * @param adults The number of adults traveling (default is 1).
    * @return A JSON string response containing flight search results.
    */

   @GetMapping("/")
   public String getIndexView() {
       return "index";
   }
   
   @GetMapping("/search")
   public String searchFlights(
           @RequestParam String origin,
           @RequestParam String destination,
           @RequestParam String departureDate,
           @RequestParam(defaultValue = "1") int adults) {
       
        String response = amadeusApiClientService.searchFlights(origin, destination, departureDate, adults);

        //parseData(response);

        return response;
   }

   public void parseData(String jsonString)
   {
       JsonObject data = new Gson().fromJson(jsonString, JsonObject.class);

       JsonArray names = data .get("data").getAsJsonArray();

       for(JsonElement element : names){
          
          JsonObject object = element.getAsJsonObject();

          System.out.println(object.get("type").getAsJsonObject().get("name").getAsString());
       } 
   }
}