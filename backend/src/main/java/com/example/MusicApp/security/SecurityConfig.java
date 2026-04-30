//package com.example.MusicApp.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                // 1. API OVERRIDE: Tell Spring to never session-manage these paths
//                .securityMatcher("/api/**")
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/spotify/**").permitAll()
//                        .anyRequest().permitAll() // Let all API calls through for testing
//                )
//                // 2. DISABLE REDIRECTS: Instead of a login page, send a 401 error
//                .exceptionHandling(exceptions -> exceptions
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setStatus(401);
//                            response.getWriter().write("Unauthorized: API access denied");
//                        })
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/Account/**", "/oauth2/**", "/login/oauth2/**",
//                                "/*.css", "/*.js", "/*.jpg", "/*.png", "/*.mp3").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(login -> login
//                        .loginPage("/Account/login")
//                        .usernameParameter("email")
//                        .passwordParameter("password")
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/Account/login")
//                        .defaultSuccessUrl("/api/music/Home", true)
//                )
//                .logout(logout -> logout.permitAll());
//
//        return http.build();
//    }
//}