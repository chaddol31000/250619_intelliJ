package com.example.ws_back.controller;

import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AuthController {
  @GetMapping("/api/auth/check")
  public ResponseEntity<Map<String,String>> checkLogin(Authentication authentication) {
    if(authentication!=null && authentication.isAuthenticated()) {
      String role = authentication.getAuthorities().stream().map(a->a.getAuthority()).findFirst().orElse("");
      String username = authentication.getName();
      Map<String, String> responseBody = Map.of("username", username, "role", role);
      return ResponseEntity.ok(responseBody);
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
  }
}
