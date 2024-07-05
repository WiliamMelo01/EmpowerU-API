package org.wiliammelo.empoweru.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Allow public access to Swagger UI resources
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                // Allow public access to H2 console
                                .requestMatchers("/h2-console/**").permitAll()

                                // Allow student, professor registration, and login for everyone
                                .requestMatchers(HttpMethod.POST, "/auth/register/student").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/register/professor").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                                // Require ADMIN role for admin registration
                                .requestMatchers(HttpMethod.POST, "/auth/register/admin").hasRole("ADMIN")

                                // Restrict operations on students and professors based on specific roles
                                .requestMatchers("/student/**").hasAnyRole("STUDENT", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/professor/{id}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/professor/{id}").hasRole("PROFESSOR")
                                .requestMatchers(HttpMethod.DELETE, "/professor/{id}").hasAnyRole("PROFESSOR", "ADMIN")

                                // Allow access to course details and operations with role restrictions
                                .requestMatchers(HttpMethod.GET, "/course/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/course/").permitAll()
                                .requestMatchers(HttpMethod.POST, "/course/").hasRole("PROFESSOR")
                                .requestMatchers(HttpMethod.DELETE, "/course/").hasAnyRole("PROFESSOR", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/course/").hasAnyRole("PROFESSOR", "ADMIN")

                                // Allow access to video details and operations with role restrictions
                                .requestMatchers(HttpMethod.GET, "/video/{courseId}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/video/{courseId}").hasAnyRole("PROFESSOR", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/video/{courseId}").hasAnyRole("PROFESSOR", "ADMIN")

                                // Restrict all other requests to authenticated users
                                .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                // Disable frame options to allow H2 console to load
                .headers().frameOptions().disable().and()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
