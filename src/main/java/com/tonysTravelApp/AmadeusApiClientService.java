package com.tonysTravelApp;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AmadeusApiClientService {
    
    private final HttpClient httpClient;
    private final AmadeusApiProperties properties;
    private String accessToken;
    private Instant tokenExpiry;

    public AmadeusApiClientService(AmadeusApiProperties properties) {
        this.httpClient = HttpClient.newHttpClient();
        this.properties = properties;
    }

    private void authenticate() throws IOException, InterruptedException {
        
        String form = "grant_type=client_credentials"
            + "&client_id=" + URLEncoder.encode(properties.getClientId(), StandardCharsets.UTF_8)
            + "&client_secret=" + URLEncoder.encode(properties.getClientSecret(), StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(properties.getTokenUrl()))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(form))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to authenticate with Amadeus API: " + response.body());
        }
        
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        this.accessToken = json.get("access_token").getAsString();

        int expiresIn = json.get("expires_in").getAsInt();

        this.tokenExpiry = Instant.now().plusSeconds(expiresIn - 60); // refresh 1 min early
    }

    private String getValidAccessToken() throws IOException, InterruptedException {
    
        if (accessToken == null || tokenExpiry == null || Instant.now().isAfter(tokenExpiry)) {
            authenticate();
        }

        return accessToken;
    }

    public String searchFlights(String origin, String destination, String departureDate, int adults) {
    
        try {
            String token = getValidAccessToken();
        
            Map<String, String> params = Map.of(
                "originLocationCode", origin,
                "destinationLocationCode", destination,
                "departureDate", departureDate,
                "adults", String.valueOf(adults),
                "max", "5"
            );

            String queryString = params.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(properties.getFlightSearchUrl() + "?" + queryString))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Failed to search flights : {}", response.body());
                return response.body();
            }

            return response.body();
        } 
        catch (IOException | InterruptedException | RuntimeException e) {
            log.error("Failed to search flights: {}", e.getMessage());
            return e.getMessage();
        }
    }
}
