package com.example.ws_back.controller;

import org.springframework.context.event.*;
import org.springframework.stereotype.*;
import org.springframework.web.socket.messaging.*;

// 스프링 이벤트 핸들러 등록
// 웹소켓에 연결되어 있다면 원래 다 찍혀 나오기 때문에 만들 필요는 없음
// 웹소켓 연결, 해제에 대한 스프링 이벤트 핸들러!
// 웹소켓은 페이지 단위로 연결함 같은 주소로 새 페이지를 만들면 연결이 종료 됐다가 다시 연결됨
  // 현재 브라우저를 벗어날 수 없음
  // 증가 버튼이나 이런 걸 누르게 되면 BACK 은 연결이 종료됐다가 다시 연결되는 걸 모르고 계속 연결함
  // 웹소켓의 단점 : 연결이 끊어지지 않음

@Component
public class WebSocketEventListener {
  @EventListener
  public void connect(SessionConnectEvent e) {
    // 웹소켓 연결이 생성된 이벤트
    System.out.println("WebSocket 연결: " + e.getMessage());
  }

  @EventListener
  public void disconnect(SessionDisconnectEvent e) {
    // 웹소켓 연결이 끊어진 이벤트
    System.out.println("WebSocket 연결 종료: " + e.getMessage());
  }
}
