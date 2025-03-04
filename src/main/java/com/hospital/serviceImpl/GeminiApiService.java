package com.hospital.serviceImpl;

import okhttp3.*;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeminiApiService {

	private static final String API_KEY = "AIzaSyBHiKgPMNlDvkpACLokU3iSfLQ7_4tudNs";
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String askGemini(String prompt) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Create request JSON body
        String jsonBody = objectMapper.writeValueAsString(Map.of(
            "contents", new Object[]{
                Map.of("parts", new Object[]{Map.of("text", prompt)})
            }
        ));

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder().url(URL).post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Error: " + response.code() + " - " + response.message();
            }
            return response.body().string();
        }
    }
	
}
