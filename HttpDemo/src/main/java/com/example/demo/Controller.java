package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class Controller {
    
    // 定義靜態的 Authorization JWT
    private static final String STATIC_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE3MjQ5MDU4NjIsImV4cCI6MTc1NjQ0MTg2MiwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.6grVjuOk28KdY6701P3SiwoSrjOFzNbjvTLhNUJOqeo";

    @GetMapping("/hello")
    public Map<String, String> sayHello() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Server received the request successfully.");
        return response;
    }

    @GetMapping("/get")
    public Map<String, String> getWithParams(
        @RequestParam(value = "param1", required = false) String param1,
        @RequestParam(value = "param2", required = false) String param2) {

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("param1", param1 != null ? param1 : "No param1 provided");
        response.put("param2", param2 != null ? param2 : "No param2 provided");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));

        return response;
    }

    
    

    @PostMapping("/post")
    public ResponseEntity<Map<String, String>> handlePostRequest(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Map<String, String> payload) {

        Map<String, String> response = new HashMap<>();

        // 驗證 Authorization 標頭
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.put("status", "error");
            response.put("message", "Missing or invalid Authorization header");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // 處理接收到的 JSON 數據
        response.put("status", "success");
        response.put("receivedParam1", payload.getOrDefault("param1", "No param1 provided"));
        response.put("receivedParam2", payload.getOrDefault("param2", "No param2 provided"));
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        response.put("authorization", authorization);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> handleAuthRequest(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody Map<String, String> payload) {

        Map<String, String> response = new HashMap<>();

        // 驗證 Authorization 標頭是否包含 "Bearer " 前綴和正確的 JWT
        if (authorization == null || !authorization.equals("Bearer " + STATIC_JWT)) {
            response.put("status", "error");
            response.put("message", "Invalid Authorization token");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // 處理接收到的 JSON 數據
        response.put("status", "success");
        response.put("message", "Authorization successful");
        response.put("receivedParam1", payload.getOrDefault("param1", "No param1 provided"));
        response.put("receivedParam2", payload.getOrDefault("param2", "No param2 provided"));
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

@PostMapping("/teachable")
public ResponseEntity<Map<String, String>> teachable(
        @RequestBody List<Map<String, Object>> payload) {

    Map<String, String> response = new HashMap<>();

        // 使用 ObjectMapper 將接收到的 Map 轉換為 JSON 格式
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonData;
    try {
        jsonData = objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
        jsonData = "Error converting data to JSON";
    }

    // 在後端打印接收到的資料
    System.out.println("Received payload: " + jsonData);

    response.put("status", "success");
    response.put("receivedData", payload.toString());
    response.put("timestamp", String.valueOf(System.currentTimeMillis()));

    return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
