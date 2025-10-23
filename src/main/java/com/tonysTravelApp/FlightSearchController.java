package com.tonysTravelApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

/**
* FlightSearchController handles flight search requests using Amadeus API based on user input.
*/
//@RestController
@Controller
public class FlightSearchController {

   List<String> originList;
   List<String> destinationList;
   List<String> adultsList;

   @ModelAttribute
   public void preLoad(Model model){
  
      // Origin                           
      originList = new ArrayList<>();
      originList.add("DUB"); // Dublin
      originList.add("LON"); // London
      originList.add("NYC"); // New York

      // Destination
      destinationList = new ArrayList<>();
      destinationList.add("CDG"); // Paris (Charles de Gaulle)
      destinationList.add("BER"); // Berlin
      destinationList.add("MXP"); // Milan (Malpensa)

      // Adults
      adultsList = new ArrayList<>();
      adultsList.add("1");
      adultsList.add("2");
      adultsList.add("3");
      adultsList.add("4");
      adultsList.add("5");
   }

   @GetMapping("/")
   public String getIndexView(Model model) {

      model.addAttribute("originList", originList);
      model.addAttribute("destinationList", destinationList);
      model.addAttribute("adultsList", adultsList);

      return "index";
   }
   
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
   
   @PostMapping(value="/search")
   public String search(@ModelAttribute("trip") Trip trip, Model model)
   {
      //model.addAttribute("getdata", trip.toString()); // debug

      String response = amadeusApiClientService.searchFlights(trip.getOrigin(), trip.getDestination(), trip.getDepartureDate(), trip.getAdults());

      model.addAttribute("response", response);

      String data = parseData(response);
      model.addAttribute("data", data);

      return "searchResults";
   }

   public String parseData(String input)
   {
      JSONObject jsonObject = new JSONObject(input);
    
      JSONArray json_data = jsonObject.getJSONArray("data");
            
      return "Size of data array: " + json_data.length();
   }

   @GetMapping("/test")
   public String test(Model model)
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

      model.addAttribute("output", output);

      return "testResults";
   }
}