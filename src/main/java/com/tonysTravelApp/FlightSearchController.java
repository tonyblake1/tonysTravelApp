package com.tonysTravelApp;

import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.json.JSONObject;

/**
* FlightSearchController handles flight search requests using Amadeus API based on user input.
*/
//@RestController
@Controller
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
           @RequestParam String origin,   // test with DUB
           @RequestParam String destination, // test with NYC
           @RequestParam String departureDate,  // test with 2025-09-01
           @RequestParam(defaultValue = "1") int adults) // test with 2
   {
       
        String response = amadeusApiClientService.searchFlights(origin, destination, departureDate, adults);

        String data = parseData(response);

        return "searchResults";
   }

   public String parseData(String input)
   {
      int output;

      JSONObject jsonObject= new JSONObject(input);
    
      output=jsonObject.getJSONArray("data")
                       .getJSONObject(0)
                       .getInt("numberOfBookableSeats");
                       

      return "Number of bookable seats: " + output;
   }

   @GetMapping("/test")
   public String test()
   {
      String input = "{\r\n" + //
                    "    \"d\": {\r\n" + //
                    "        \"__metadata\": {\r\n" + //
                    "            \"type\": \"typehere\",\r\n" + //
                    "            \"uri\": \"urihere\"\r\n" + //
                    "        },\r\n" + //
                    "        \"BOLNR\": \"00170548\",\r\n" + //
                    "        \"ToCheck_BOL_SO\": {\r\n" + //
                    "            \"results\": [\r\n" + //
                    "                {\r\n" + //
                    "                    \"__metadata\": {\r\n" + //
                    "                        \"type\": \"typehere\",\r\n" + //
                    "                        \"uri\":\"urihere\"\r\n" + //
                    "                    },\r\n" + //
                    "                    \"DOC_NUM\": \"0011998\",\r\n" + //
                    "                    \"BOLNR\": \"00263334\",\r\n" + //
                    "                    \"ToCheck_BOL_SO_DN\": {\r\n" + //
                    "                        \"results\": [\r\n" + //
                    "                            {\r\n" + //
                    "                                \"__metadata\": {\r\n" + //
                    "                                    \"type\": \"typeheree\",\r\n" + //
                    "                                    \"uri\": \"urihere\"\r\n" + //
                    "                                },\r\n" + //
                    "                                \"DOC_NUM\": \"0011278\",\r\n" + //
                    "                                \"DEL_NUM\": \"0805137\",\r\n" + //
                    "                                \"SHIP_NUM\": \"0011716\",\r\n" + //
                    "                                \"BOLNR\": \"26300777\"\r\n" + //
                    "                            }\r\n" + //
                    "                        ]\r\n" + //
                    "                    }\r\n" + //
                    "                }\r\n" + //
                    "            ]\r\n" + //
                    "        }\r\n" + //
                    "    }\r\n" + //
                    "}";
      
      String output="";

      JSONObject jsonObject= new JSONObject(input);
    
      output=jsonObject.getJSONObject("d")
                       .getJSONObject("ToCheck_BOL_SO")
                       .getJSONArray("results")
                       .getJSONObject(0)
                       .getJSONObject("ToCheck_BOL_SO_DN")
                       .getJSONArray("results")
                       .getJSONObject(0)
                       .getString("DOC_NUM");

      return "testResults";
   }
}