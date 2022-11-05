package com.example.shoppingmall_restapi.config;


import com.example.shoppingmall_restapi.config.jwt.JwtAccessDeniedHandler;
import com.example.shoppingmall_restapi.config.jwt.JwtAuthenticationEntryPoint;
import com.example.shoppingmall_restapi.config.jwt.JwtSecurityConfig;
import com.example.shoppingmall_restapi.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) { //하위 모든 요청과 파비콘은 모두 무시하는 설정
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable 토큰방식을 사용하기 때문이다.
        http.csrf().disable();

        // CORS
        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of("http://localhost:3000"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        });
        http
                .authorizeRequests() //HttpServletRequest를 사용하 요청제한 접근 설정
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll();


        http
                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)


                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()

                .antMatchers("/swagger-ui/**", "/v3/**", "/test").permitAll() // swagger
                .antMatchers(HttpMethod.GET, "/image/**").permitAll() // images
                .antMatchers("/api/sign-up/**", "/api/sign-in", "/api/reissue").permitAll() // auth


                .antMatchers(HttpMethod.POST, "/api/products").access("hasRole('ROLE_SELLER')")
                .antMatchers(HttpMethod.GET, "/api/products").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/products/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/products/{id}/likes").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/products/{id}").access("hasRole('ROLE_SELLER')")
                .antMatchers(HttpMethod.DELETE, "/api/products/{id}").access("hasRole('ROLE_SELLER')")

                .antMatchers(HttpMethod.POST, "/api/reviews").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.GET, "/api/products/{id}/reviews").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/reviews/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/reviews/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")

                .antMatchers(HttpMethod.POST, "/api/carts").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/api/carts").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.DELETE, "/api/carts/{cartItemId}").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.POST, "/api/carts/buying").access("hasRole('ROLE_USER')")

                .antMatchers(HttpMethod.PUT, "/api/members").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/api/members").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.DELETE, "/api/members").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/api/members/likes").access("hasRole('ROLE_USER')")

                .antMatchers(HttpMethod.GET, "/api/buyHistories").access("hasRole('ROLE_USER')")
                .antMatchers(HttpMethod.GET, "/api/sellHistories").access("hasRole('ROLE_SELLER')")

                .antMatchers(HttpMethod.POST, "/api/report/users").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/report/products").access("hasRole('ROLE_USER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")




                .anyRequest().hasAnyRole("ROLE_ADMIN")
//                .anyRequest().authenticated() // 나머지는 전부 인증 필요
//                .anyRequest().permitAll()   // 나머지는 모두 그냥 접근 가능

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }
}