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

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String STUDENT_ROLE = "STUDENT";
    private static final String PROFESSOR_ROLE = "PROFESSOR";

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
                                .requestMatchers(HttpMethod.POST, "/auth/register/admin").hasRole(ADMIN_ROLE)

                                // Restrict operations on professors based on specific roles
                                .requestMatchers(HttpMethod.GET, "/professor/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/professor/").hasRole(ADMIN_ROLE)
                                .requestMatchers(HttpMethod.PUT, "/professor/").hasRole(PROFESSOR_ROLE)
                                .requestMatchers(HttpMethod.DELETE, "/professor/").hasRole(PROFESSOR_ROLE)
                                .requestMatchers(HttpMethod.DELETE, "/professor/{id}").hasRole(ADMIN_ROLE)

                                // Restrict operations on students based on specific roles
                                .requestMatchers(HttpMethod.GET, "/student/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/student/").hasRole(ADMIN_ROLE)
                                .requestMatchers(HttpMethod.PUT, "/student/").hasRole(STUDENT_ROLE)
                                .requestMatchers(HttpMethod.DELETE, "/student/").hasRole(STUDENT_ROLE)
                                .requestMatchers(HttpMethod.DELETE, "/student/{id}").hasRole(ADMIN_ROLE)

                                // Allow access to course details and operations with role restrictions
                                .requestMatchers(HttpMethod.GET, "/course/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/course/").permitAll()
                                .requestMatchers(HttpMethod.POST, "/course/").hasRole(PROFESSOR_ROLE)
                                .requestMatchers(HttpMethod.DELETE, "/course/{id}").hasAnyRole(PROFESSOR_ROLE, ADMIN_ROLE)
                                .requestMatchers(HttpMethod.PUT, "/course/{id}").hasAnyRole(PROFESSOR_ROLE, ADMIN_ROLE)

                                // Allow access to video details and operations with role restrictions
                                .requestMatchers(HttpMethod.GET, "/video/{courseId}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/video/{courseId}").hasAnyRole(PROFESSOR_ROLE, ADMIN_ROLE)
                                .requestMatchers(HttpMethod.DELETE, "/video/{videoId}").hasAnyRole(PROFESSOR_ROLE, ADMIN_ROLE)

                                // Restrict all other requests to authenticated users
                                .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
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
