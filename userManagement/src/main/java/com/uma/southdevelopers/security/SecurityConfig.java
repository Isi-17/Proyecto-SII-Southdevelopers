package com.uma.southdevelopers.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // We don't need CSRF for this example
        http
                .cors()
                .and()
                .csrf().disable()
                // dont authenticate this particular request
                .authorizeHttpRequests()
                .requestMatchers("/usuarios/login")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios") // TODO: sin autorizacion para las pruebas, pedir autorizacion
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios/passwordreset") // TODO: esta abierto para las pruebas, pedir token
                .permitAll()
		        .requestMatchers(HttpMethod.GET, "/usuarios/**")
                .hasAnyRole("ROLE_RESPONSABLE_AULA","ROLE_VIGILANTE_AULA", "RESPONSABLE_AULA","VIGILANTE_AULA",
                        "VICERRECTORADO", "ROLE_VICERRECTORADO", "ROLE_CORRECTOR", "CORRECTOR")
                .requestMatchers(HttpMethod.PUT, "/usuarios/**")
                .hasAnyRole("VICERRECTORADO", "ROLE_VICERRECTORADO")
                .requestMatchers(HttpMethod.DELETE, "/usuarios/**")
                .hasAnyRole("VICERRECTORADO", "ROLE_VICERRECTORADO")
                .requestMatchers(HttpMethod.GET, "/usuarios")
                .hasAnyRole("ROLE_RESPONSABLE_AULA","ROLE_VIGILANTE_AULA", "RESPONSABLE_AULA","VIGILANTE_AULA",
                        "VICERRECTORADO", "ROLE_VICERRECTORADO", "ROLE_CORRECTOR", "CORRECTOR")
                .anyRequest()
                .authenticated()
                .and()
                // make sure we use stateless session; session won't be used to
                // store user's state.
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
