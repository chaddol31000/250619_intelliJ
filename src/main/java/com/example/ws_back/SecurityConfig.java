package com.example.ws_back;

import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.provisioning.*;
import org.springframework.security.web.*;
import org.springframework.security.web.access.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.web.cors.*;

import java.util.Arrays;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  // 401 오류 처리 (로그인이 필요하다)
  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;
  // 403 오류 처리 (권한 오류)
  @Autowired
  private AccessDeniedHandler accessDeniedHandler;
  // 로그인 성공 - 200
  @Autowired
  private AuthenticationSuccessHandler authenticationSuccessHandler;
  // 로그인 실패 - 409 or 401로 응답 (현재는 409)
  @Autowired
  private AuthenticationFailureHandler authenticationFailureHandler;
  // 로그아웃 성공 - 200으로 응답
  @Autowired
  private LogoutSuccessHandler logoutSuccessHandler;

  // DB 사용하지 않고 메모리에 사용자 정보를 때려 박음
  // passwordEncoder 는 Application 에 했음
  @Bean
  public UserDetailsService users(PasswordEncoder passwordEncoder) {
    UserDetails user1 = User.builder().username("spring").password(passwordEncoder.encode("1234")).roles("USER").build();
    UserDetails user2 = User.builder().username("summer").password(passwordEncoder.encode("1234")).roles("USER").build();
    UserDetails user3 = User.builder().username("winter").password(passwordEncoder.encode("1234")).roles("HOSPITAL").build();

    UserDetails h1 = User.builder().username("munhak").password(passwordEncoder.encode("1234")).roles("HOSPITAL").build();
    return new InMemoryUserDetailsManager(user1, user2, user3, h1);
  }
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity config) throws Exception {
    // csrf : MVC 방식에서 타임리프 파일을 위조, 변조하는 것을 막기 위해 사용한다
    //        사용자가 작업한 html 파일이 서버가 보내준 파일이 맞는지, 혹시 사용자가 html 을 조작하지 않았는지
    //        확인하기 위한 랜덤 문자열이라고 생각하면 됨
    //        화면이 없는 rest 에는 의미없는 개념이니 꺼준 거임
    config.cors(cors->cors.configurationSource(corsConfigurationSource()));
    config.csrf(csrf->csrf.disable());
    // 화면에 아이디와 비밀번호를 입력해서 로그인하는 formLogin 을 활성화
    config.formLogin(form->form.loginPage("/login").loginProcessingUrl("/login")
      .successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler));
    config.logout(logout->logout.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler));
    config.exceptionHandling(handler->
      handler.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint));
    return config.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(Arrays.asList("*"));
    config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE"));
    config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    config.setAllowCredentials(true);

    org.springframework.web.cors.UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    src.registerCorsConfiguration("/**", config);
    return src;
  }
}
