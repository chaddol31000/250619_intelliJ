package com.example.ws_back;

// STOMP 웹소켓 설정

import org.springframework.context.annotation.*;
import org.springframework.messaging.simp.config.*;
import org.springframework.web.socket.config.annotation.*;


// @Configuration : 스프링 설정 파일
// @EnableWebSocketMessageBroker : STOMP 기반 (텍스트 메시징 전용) 웹소켓 설정
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // 자바스크립트로 웹소켓 접속하는 주소 (ex. /member/login)
    // 클라이언트는 SockJS 를 이용해 /ws 주소로 접속하게 된다
    registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // 웹소켓 서비스 주소(ex. 메시지를 주고받기 등 | /post/** , /member/**)
    // 두 가지 주소를 등록하게 됨
      // 메시지를 구독하는 주소
      // 메시지를 발행하는 주소

    // 클라이언트가 메시지를 구독하는 주소는 /sub/**
    registry.enableSimpleBroker("/sub");
    // 클라이언트가 메시지를 보내는 주소는 /pub/**
    registry.setApplicationDestinationPrefixes("/pub");

    // A 와 B 가 채팅을 한다면
    // 각자 채팅 메시지를 서버로 보내야한다 (발행)
    // 서버가 보낸 채팅 메시지를 수신해야한다 (구독)
  }
}
