package com.example.ws_back;

import lombok.*;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  // 401 오류 처리 (로그인이 필요하다)
  private final AuthenticationEntryPoint authenticationEntryPoint;
  // 403 오류 처리 (권한 오류)
  private final AccessDeniedHandler accessDeniedHandler;
  // 로그인 성공 - 200
  private final AuthenticationSuccessHandler authenticationSuccessHandler;
  // 로그인 실패 - 409 or 401로 응답 (현재는 409)
  private final AuthenticationFailureHandler authenticationFailureHandler;
  // 로그아웃 성공 - 200으로 응답
  private final LogoutSuccessHandler logoutSuccessHandler;

  // DB 사용하지 않고 메모리에 사용자 정보를 때려 박음
  // passwordEncoder 는 Application 에 했음
  @Bean
  public UserDetailsService users(PasswordEncoder passwordEncoder) {
    UserDetails user1 = User.builder().username("spring").password(passwordEncoder.encode("1234")).roles("USER").build();
    UserDetails user2 = User.builder().username("summer").password(passwordEncoder.encode("1234")).roles("USER").build();
    UserDetails user3 = User.builder().username("winter").password(passwordEncoder.encode("1234")).roles("USER").build();

    UserDetails h1 = User.builder().username("munhak").password(passwordEncoder.encode("1234")).roles("HOSPITAL").build();
    return new InMemoryUserDetailsManager(user1, user2, user3, h1);
  }
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity config) throws Exception {
    config.csrf(csrf->csrf.disable());
    config.formLogin(form->form.loginPage("/login").loginProcessingUrl("/login")
      .successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler));
    config.logout(logout->logout.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler));
    config.exceptionHandling(handler->
      handler.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint));
    return config.build();
  }

  @Bean
  CorsConfigurationSource configurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(Arrays.asList("*"));
    config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
    config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    src.registerCorsConfiguration("/**", config);
    return src;
  }
}
