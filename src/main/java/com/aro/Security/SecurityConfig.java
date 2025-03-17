package com.aro.Security;

import com.aro.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenFilter jwtTokenFilter) {
        this.customUserDetailsService = userDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // so this will need the UserDetailsService
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/register").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource orsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

// now i have to make the whole security from scratch


// there is something called which is activated when there is special request to the server like with some content, authorization headers
// or put update or delete request so this request is gone to the spring security to handle this we have to make the CorsConfigurationSource

// HOW THE AUTHENTICATION WORKS WITH THE daoAuthenticationProvider ?
// User enters a password in the login form.
// Spring Security calls customUserDetailsService.loadUserByUsername(username), which fetches the user’s stored details
// (including the hashed password).
// DaoAuthenticationProvider hashes the entered password using passwordEncoder() (which is BCrypt in your case).
// It compares the hashed entered password with the hashed password stored in the database.
// If they match ✅ → Authentication succeeds. Otherwise ❌ → Login fails.