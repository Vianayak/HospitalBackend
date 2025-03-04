package com.hospital.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.model.DoctorsInfo;
import com.hospital.service.DoctorsInfoService;
import com.hospital.serviceImpl.GoogleAuthService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
@RequestMapping("/api/chatBot")
@CrossOrigin(origins = "*")
public class ChatBotController {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent";

    @Autowired
    private GoogleAuthService googleAuthService;
    
    @Autowired
	private DoctorsInfoService doctorsInfoService;

    @PostMapping("/generate-response")
    public ResponseEntity<?> generateResponse(@RequestBody String userInput) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        
        String modifiedPrompt=userInput+".I want just which specialization doctor can treat names nothing else just one word names";

        String jsonRequest = "{ \"contents\": [{ \"role\": \"user\", \"parts\": [{ \"text\": \"" + modifiedPrompt + "\" }] }] }";

        System.out.println("Sending request to: " + API_URL);
        System.out.println("Request JSON: " + jsonRequest);

        String accessToken = googleAuthService.getAccessToken(); // Get the latest token

        okhttp3.RequestBody body = okhttp3.RequestBody.create(
            jsonRequest, MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                System.out.println("Response: " + responseBody);

                // Extract specialization names from the JSON response
                List<String> specializations = extractSpecializations(responseBody);
                
                List<DoctorsInfo> doctors = doctorsInfoService.getAllDoctorsBySpecialization(
                        specializations
                    );

                return ResponseEntity.ok(doctors);
            } else {
                System.out.println("Error Response: " + response.code() + " - " + response.message());
                return ResponseEntity.status(response.code()).body(response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    

	private List<String> extractSpecializations(String responseBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);

        // Navigate to candidates -> content -> parts -> text
        JsonNode candidates = rootNode.path("candidates");
        if (candidates.isArray() && candidates.size() > 0) {
            JsonNode content = candidates.get(0).path("content");
            JsonNode parts = content.path("parts");
            if (parts.isArray() && parts.size() > 0) {
                String text = parts.get(0).path("text").asText();
                
                // Split text by newlines and return as list
                return Arrays.stream(text.split("\n"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty()) // Remove empty lines
                        .collect(Collectors.toList());
            }
        }
        return List.of();
    }
	
	
	
	
}
