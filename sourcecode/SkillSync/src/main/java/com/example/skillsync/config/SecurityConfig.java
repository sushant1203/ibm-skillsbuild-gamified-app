package com.example.skillsync.config;

import com.example.skillsync.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //defines the class as a spring configuration class
@EnableWebSecurity //enables spring web security for the application
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    //injecting CustomUserDetailsService into to the class constructor to allow for a specific user authentication
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request ->
                    request.requestMatchers("/register", "/login", "/", "stylesheet.css", "*.png", "login.css","*.jpg","*.ico").permitAll() //allows access to these three endpoints
                            .anyRequest().authenticated() //if any other endpoint(request), it requires authentication
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login") //access to specific login page
                        .defaultSuccessUrl("/dashboard") //redirects to landing page after successful login
                        .permitAll() //allow access to all for log in page
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") //redirects to log in page after logout
                        .invalidateHttpSession(true) // Invalidates the session
                        .deleteCookies("JSESSIONID") // Deletes session cookies
                        .permitAll() //allow logout for all users
                )
                .userDetailsService(userDetailsService); //for authentication


        return http.build(); //builds and returns the security configuration
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //encryption of passwords using Bcrypt hashing
    }
}