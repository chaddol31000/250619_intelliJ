package com.example.ws_back.security;

import com.example.ws_back.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.access.*;
import org.springframework.security.web.access.*;
import org.springframework.stereotype.*;

import java.io.*;

// 403(권한 없음) 을 처리하는 핸들러
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    ResponseUtil.sendJsonResponse(response, 403, "작업 권한이 없습니다");
  }
}