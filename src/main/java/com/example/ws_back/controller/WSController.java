package com.example.ws_back.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.time.*;
import java.time.format.*;

@Controller
public class WSController {
  // HTTP 컨트롤러는 클라이언트 → 서버 주소만 지정, 컨트롤러 마지막에 응답
  // 클라이언트 → 서버 = /pub
  // 서버 → 클라이언트 = /sub

  // 클라이언트에 메시지를 보내는 객체 → 전체 메시지 or 사용자를 지정해서
  @Autowired
  private SimpMessagingTemplate tpl;

  // /pub 가 생략된 발행하는 주소
  // 클라이언트가 /pub/job1 로 메시지를 보내면 실행된다
  // @MessageMapping("/job1")
  // 스프링 스케줄러 : 정해진 시간, 정해진 간격마다 실행하는 것
    // 10초마다 1번 씩 job1 을 실행해라
    // fixedDelay 또는 cron 식 (유닉스 스케줄링 표현식) → cron maker 에서 작성
  // @Scheduled(cron="0 0/1 * 1/1 * ?")

  @Scheduled(fixedDelay = 10000)
  public void job1() {
    LocalDateTime now = LocalDateTime.now();
    // 날짜를 문자열로 변환
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh시 mm분 ss초");
    String time = dtf.format(now);

    // /pub/job1 로 발행하면 /sub/job1 로 수신할 수 있다
    tpl.convertAndSend("/sub/job1", time);
  }

  @MessageMapping("/job2")
  public void job2(String message, Principal principal) {
    String username = principal==null? "GUEST" : principal.getName();
    System.out.println(message);
    tpl.convertAndSend("/sub/job2", username + ":" + message);

  }

  // @PreAuthorize("isAuthenticated()")
  @PostMapping("/api/message")
  public ResponseEntity<Void> job3 (String receiver, String message) {
    String sender = "hana";
    if(sender.equals(receiver))
      return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    // receiver 에게만 웹소켓 메시지를 전송
    // /sub/job3 를 수신주소로 특정 사용자에게 메시지를 보낸다 → /user/sub/job3 로 수신
    tpl.convertAndSendToUser(receiver, "/sub/job3", sender + "메시지");
    return ResponseEntity.ok(null);
  }
  // 클라이언트 → 서버 /pub
  // 서버 → 클라이언트
}
