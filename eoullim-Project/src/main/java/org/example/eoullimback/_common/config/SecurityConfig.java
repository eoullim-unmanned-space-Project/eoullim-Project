package org.example.eoullimback._common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig  {

    /**
     * @Bean을 스프링에 등록
     * SecurityFilterChain 스프링 컨테이너에 객체 등록
     *
     * SecurityFilterChain이란?
     * 요청이 들어올 때 거치는 보안 검사
     * 예) "로그인을 했어요?" ***중요*** --→ "CSRF 토큰이 있어요? --→ "권한 있어요?"
     *
     * authorizeHttpRequests --→ 어떤 요청을 허용할지 설정
     * auth.anyRequest().permitAll() --→ "모든요청(GET, POST, PUT, DELETE)전부"
     *
     * *** 나중에 ***
     * GET은 허용하고 나머지는 설정
     * .requestMatchers("/", "/login"/, "signup").permitAll         ---→ (URL경로 설정 및 권한 허용)
     * .requestMatchers("/css/**", "/js/**", "/images/**")          ---→ (정적 파일 설정)
     * .requestMatchers(HttpMethod.GET, "/api/posts").permitAll()   ---→ (HTTP 메서드별 설정)
     * .requestMatchers("/admin/**").hasRole("ADMIN")               ---→ (역할기반 설정) ** ADMIN ** 역할
     * .requestMatchers("/admin/**").hasRole("USER")               ---→ (역할기반 설정) ** USER ** 역할
     * .anyRequest().authenticated() --→ 위에 경로를 제외한 나머지는 로그인이 필요 설정
     *
     *
     * *** 총 정리 권한 설정 ***
     * .permitAll()        ---→ 누구나 접근 가능
     * .authenticated()    ---→ 로그인한 사용자만
     * .hasRole()          ---→ 특정 역할만
     * .hasAnyRole         ---→ 여러 역할
     * .denyAll()          ---→ 모두 거부
     * .anonymous()        ---→ 익명 사용자만 (로그인 안 한 사람)
     *
     * *************
     *
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/css/**", "/js/**","/map/**", "/images/**", "/img/**", "/favicon.ico", "/error").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/auth/signup", "/auth/login", "/auth/logout", "/error-direct/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/chat").permitAll()

                        .requestMatchers(HttpMethod.GET, "/auth/find-id").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/find-id/send").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/find-id/verify").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login-id/recovery").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/find-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/find-password/send-code").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/password/verify-code").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/password/reset").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/password/reset").permitAll()

                        // 사용자 - 프로필
                        .requestMatchers(HttpMethod.GET, "/user/kakao").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/profile").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/user/profile").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/user/profile/edit").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/user/profile/image").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/user/profile/leave").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/user/profile/verify-password").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/user/profile/bookings").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/user/profile/bookings").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/user/profile/payments").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/user/profile/refund").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/user/profile/use-qrCode/{id}").hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "/api/user/password-verify").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/user/email-find-verifications").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/email-find-verifications/verify").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/password-verify").permitAll()

                        // 부킹
                        .requestMatchers(HttpMethod.POST, "/api/user/bookings").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/user/bookings/amount").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/user/bookings/cancel/{bookingCode}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/user/bookings/detail").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/user/bookings/complete").hasRole("USER")

                        // 페이먼츠 - 결제
                        .requestMatchers(HttpMethod.POST, "/api/user/payments/prepare").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/user/payments/complete").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/user/payments/cancel").hasRole("USER")

                        .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")

                        // QnA
                        .requestMatchers(HttpMethod.GET, "/public/qnas").permitAll() // R
                        .requestMatchers(HttpMethod.GET, "/public/qnas/{qnaId}").permitAll() // R 단일상세

                        // QnA 마이페이지
                        .requestMatchers(HttpMethod.POST, "/user/qna").hasRole("USER")// C
                        .requestMatchers(HttpMethod.GET, "/user/qna").hasRole("USER") // R
                        .requestMatchers(HttpMethod.GET, "/user/qna/{qnaId}").hasRole("USER") // R 단일상세
                        .requestMatchers(HttpMethod.GET, "/user/qna/{qnaId}/edit").hasRole("USER") // U 화면요청
                        .requestMatchers(HttpMethod.POST, "/qna/{qnaId}/update").hasRole("USER") // U
                        .requestMatchers(HttpMethod.POST, "/user/qna/{qnaId}/delete").hasRole("USER") // D

                        // QnA 관리자
                        .requestMatchers(HttpMethod.GET, "/admin/qnas").hasRole("ADMIN") // R
                        .requestMatchers(HttpMethod.GET, "/admin/qnas/{qnaId}").hasRole("ADMIN") // R 단일상세
                        .requestMatchers(HttpMethod.POST, "/admin/qnas/{qnaId}/delete").hasRole("ADMIN") // D

                        // QnA 관리자 api
                        .requestMatchers(HttpMethod.GET, "/api/admin/qnas/count").hasRole("ADMIN") // R 관리자 대시보드 문의 수

                        // Comment 관리자
                        .requestMatchers(HttpMethod.POST, "/admin/qnas/{qnaId}/comments/new").hasRole("ADMIN") // C
                        .requestMatchers(HttpMethod.POST, "/admin/comments/{commentId}/delete").hasRole("ADMIN") // D

                        // 마이페이지 리뷰
                        .requestMatchers(HttpMethod.GET, "/rooms/{roomId}/reviews/new").hasAnyRole("ADMIN", "USER") // C 리뷰 생성 화면
                        .requestMatchers(HttpMethod.POST, "/rooms/{roomId}/reviews").hasRole("USER") // C
                        .requestMatchers(HttpMethod.GET, "/reviews/{reviewId}/update").hasRole("USER") // U 화면요청
                        .requestMatchers(HttpMethod.POST, "/reviews/{reviewId}").hasRole("USER") // U
                        .requestMatchers(HttpMethod.POST, "/rooms/reviews/{reviewId}/delete").hasRole("USER") // D
                        .requestMatchers(HttpMethod.GET, "/user/reviews").hasRole("USER") // R

                        // 마이페이지 리뷰 api
                        .requestMatchers(HttpMethod.GET, "/api/user/reviews").hasRole("USER") // R 리뷰리스트
                        .requestMatchers(HttpMethod.DELETE, "/api/user/reviews/{reviewId}").hasRole("USER") // D

                        // 관리자 리뷰
                        .requestMatchers(HttpMethod.GET, "/admin/reviews").hasRole("ADMIN") // R

                        // 관리자 리뷰 api
                        .requestMatchers(HttpMethod.GET, "/api/admin/reviews").hasRole("ADMIN") // R
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/reviews/{reviewId}").hasRole("ADMIN") // D
                        .requestMatchers(HttpMethod.GET, "/api/admin/reviews/avg-rating").hasRole("ADMIN") // R 대시보드 평균 별점

                        // 알림
                        .requestMatchers(HttpMethod.GET, "/notifications").hasRole("USER") // R 나의 알림 리스트
                        .requestMatchers(HttpMethod.GET, "/notifications/qr").hasRole("USER") // R 메세지 URL - Qr 확인

                        // 공지사항
                        .requestMatchers(HttpMethod.GET, "/public/notices").permitAll() // R
                        .requestMatchers(HttpMethod.GET, "/public/notices/{noticesId}").permitAll() // R 단일상세

                        // 공지사항 관리자
                        .requestMatchers(HttpMethod.GET, "/admin/notices").hasRole("ADMIN") // R
                        .requestMatchers(HttpMethod.GET, "/admin/notices/{noticesId}").hasRole("ADMIN") // R 단일상세
                        .requestMatchers(HttpMethod.GET, "/admin/notices/new").hasRole("ADMIN") // C 작성 화면요청
                        .requestMatchers(HttpMethod.POST, "/admin/notices/new").hasRole("ADMIN") // C
                        .requestMatchers(HttpMethod.GET, "/admin/notices/{noticesId}/edit").hasRole("ADMIN") // U 화면요청
                        .requestMatchers(HttpMethod.POST, "/admin/notices/{noticesId}/edit").hasRole("ADMIN") // U
                        .requestMatchers(HttpMethod.POST, "/admin/notices/{noticesId}/delete").hasRole("ADMIN") // D

                        // 어드민
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 전체 이용
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()

                        .anyRequest().authenticated()
                )

                // csrf cookie 방지 설정
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation().migrateSession()
                )

                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/public", true)
                        .failureHandler((request, response, exception) -> {
                            if (exception instanceof LockedException) {
                                response.sendRedirect("/error-direct/account-locked");
                            } else if (exception instanceof DisabledException) {
                                response.sendRedirect("/error-direct/account-disabled");
                            } else {
                                response.sendRedirect("/error-direct/login-fail");
                            }
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/public")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                        .permitAll()
                )
                .exceptionHandling(conf -> conf
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        request.getRequestDispatcher("/error-direct/403").forward(request, response);
                    })
                    .authenticationEntryPoint((request, response, authException) -> {
                        request.getRequestDispatcher("/error-direct/401").forward(request, response);
                    })
                )
                .httpBasic(basic -> basic.disable());
        return http.build();
    }

}