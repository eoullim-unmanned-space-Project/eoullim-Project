package org.example.eoullimback._common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
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
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/img/**", "/favicon.icon").permitAll()
                        .requestMatchers("/webjars/**").permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated()
                )

                // csrf cookie 방지 설정
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                .sessionManagement(session -> session
                                .sessionFixation().migrateSession()
                                .maximumSessions(1)
                                .maxSessionsPreventsLogin(false)
                        )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
//               // *** 권한 설정 ***
//                .authorizeHttpRequests(auth ->
//                        // 정적 리소스 작성
//                        auth.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
//                        // 공개 페이지
//                        .requestMatchers("/main").permitAll()
//                        // 회원 기능 (로그인 안 한 사람만)
//                        .requestMatchers("/login", "/signup").anonymous()
//                        // 생성/수정/삭제 (로그인 필요)
//                        .requestMatchers("/api/bookings").authenticated()
//                        // 관리자 페이지 권한 설정
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        // 나머지
//                        .anyRequest().authenticated()
//                )
                /**
                 * *** CSRF 설정 ***
                 * csrf                 ---> CSRF 설정을 시작
                 * csrfTokenRepository  ---> CSRF 토큰을 어디에 저장할건지 Repository(저장소)
                 *
                 * !!!!!!!! 매우 중요 !!!!!!!
                 * **** HttpOnly = true(기본 값) ****
                 * 브라우저 쿠기 설정 : HttpOnly
                 * - JavaScript로 쿠기 읽기 불가능
                 * - document.cookie를 사용하여 접근이 불가능
                 * ---> XSS 공격을 방어한다
                 *
                 * ***** HttpOnly = false(설정) *****
                 * - JavaScript로 쿠기 읽기 가능
                 * - document.cookie를 사용하여 접근 가능
                 * ---> 비동기(AJAX, FETCH) 요청에 사용 가능
                 *
                 * ****** 전체 동작 ******
                 * 1. 사용자: GET /page 요청
                 * 2. Spring Security: CSRF토큰 자동 생성 --> "a232323b323"
                 * 3. 쿠키에 저장 : XSRF-TOKEN=a232323b323
                 * 4. 브라우저에게 쿠키 전송
                 *
                 * ***** 사용 방법  SSR OR 비동기(AJAX, FETCH)*******
                 * <! -- SSR은 FORM을 사용 -->
                 * - FORM은 type="hidden"이라는 것을 이용해서 값을 숨겨서 받을 수 있다
                 * 사용법 --> <input type="hidden" name="{{_csrf.parameterName}}" value="{{_csrf.token}}"></input>
                 * - 이렇게 토큰을 hidden 값에 넣고 같이 엔드포인트에 전송해준다.
                 *
                 * <! -- 비동기(FETCH, AJAX) -->
                 *
                 * <script>
                 * - 쿠기를 통해서 토큰을 가져온다
                 * const token = getCookie('XSRF-TOKEN');
                 *
                 * - 비동기를 처리할때 headers에 같이 담아서 보내준다
                 * await fetch ('/api/endpoint', {
                 *    method: 'POST',
                 *    headers: { 'X-XSRF-TOKEN': token }
                 * });
                 *
                 * </script>
                 *
                 * **** Customizer.withDefaults() 사용 ****
                 * 1. cookie의 탈취 가능성이 있기 때문에 --> XSS
                 * 2. 우리 프로젝트는 기본 세션 방식을 사용
                 * 3.JS에 /api-client.js 폴더를 만들어 CSRF 자동 처리
                 * 4. HTML Layout 헤더에 <meta></meta> 태그 를 사용하여 CSRF 토큰을 공용으로 사용 예정
                 * EX) <meta name="_csrf" content="{{_csrf.token}}">
                 *    <meta name="_csrf_header" content="{{_csrf.headerName}}">
                 *
                 * 5. CSRF 토큰 meta 헤더에서 가져오기
                 *  function getCsrfToken() {
                 *      !! _csrf로 값을 가져온다
                 *      return document.querySelector('meta[name="_csrf"]').content;
                 *  }
                 *
                 *  헤더 가져오기
                 *  function getCsrfHeader() {
                 *      return document.querySelector('meta[name="_csrf_header"]').content;
                 *  }
                 *
                 * EX) 비동기 POST 요청 예시
                 * async function post(url, data) {
                 *     const res = await fetch(url, {
                 *         method: 'POST',
                 *         headers: {
                 *             'Content-Type': 'application/json',
                 *             [getHeader()]: getToken() <--------가져온 토큰을 같이 담아서 백엔드 엔드포인트에 전달한다
                 *         },
                 *         body: JSON.stringify(data)
                 *     });
                 *     return res.json();
                 * }
                 *
                 *
                 * 6. 기존 코드에 csrf-util.js만들어서 사용
                 * 기존 비동기 headers에  [getHeader()]: getToken() 이 부분을,
                 * 채워 넣어야하는데 매번 넣기 힘들기 때문에 utils 함수 생성
                 *
                 * 7. utils 함수 생성
                 * function getCsrfHeaders() {
                 *     const token = document.querySelector('meta[name="_csrf"]').content;
                 *     const header = document.querySelector('meta[name="_csrf_header"]').content;
                 *
                 *     return {
                 *         [header]: token
                 *     };
                 * }
                 *
                 * 8. 수정 후
                 * fetch('/api/posts', {
                 *     method: 'POST',
                 *     headers: {
                 *         'Content-Type': 'application/json',
                 *         ...getCsrfHeaders()  // 이 유틸 함수만 추가하면 끝
                 *     },
                 *     body: JSON.stringify(data)
                 * })
                 *
                 * 9. 우선 순위 !! POST, PUT, PATCH !!
                 *
                 */
//                .csrf(Customizer.withDefaults())
//                .build();
    }

    /**
     * CORS란?
     * - 내가 만든 서버의 자원을 아무나 못 가져가게 만드는 보안 정책
     *
     * 1. 출처(Origin)이란?
     * - 브라우저는 보통 3가지가 모두 같아야 같은 출처라고 판단
     *  @ 프로토콜 (Http, Https)
     *  @ 도메인 (naver.com, https://www.google.com)
     *  @ 포트번호 (:8080, 3000)
     *  --> 이 중 하나라도 다르다면 ***다른 출처***가 된다
     *
     *  ** 우리 프로젝트는 SSR + 비동기(FETCH) 서버 포트번호: 8080 **
     *
     */
