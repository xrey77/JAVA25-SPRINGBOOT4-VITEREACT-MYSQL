package com.java25.java25.springboot4.mysql.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.java25.java25.springboot4.mysql.services.JwtUserDetailsService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.DispatcherType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String secretKey;
					  
    @Bean
    public UserDetailsService userDetailsService() {
    	return new JwtUserDetailsService();
    }
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    return config.getAuthenticationManager();
	}	
 	  		    
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomJwtAuthenticationEntryPoint authEntryPoint) throws Exception {
		
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                    .requestMatchers(
                    	    "/v3/api-docs/**",
                    	    "/api-docs/**",
                    	    "/swagger-ui/**",
                            "/v3/api-docs/swagger-config",                  	    
                    	    "/swagger-ui.html",
                    	    "/swagger-resources/**",
                    	    "/webjars/**"
                    	).permitAll()                                        
                    .requestMatchers("/auth/**", "/public/**", "/take/**", "/error").permitAll()                
                    .requestMatchers("/about", "/contact", "/profile", "/productlist", "/productcatalog", "/productsearch").permitAll()
                    .requestMatchers("/", "/index.html", "/static/**", "/react/**", "/images/**", "/users/**", "/products/**").permitAll()
            		.anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.decoder(jwtDecoder()))
                    .authenticationEntryPoint(authEntryPoint)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        return http.build();
    }	
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey spec = Keys.hmacShaKeyFor(keyBytes);        
        return NimbusJwtDecoder.withSecretKey(spec).build();
    }	
        
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	   return (web) -> web.ignoring()
	     .requestMatchers("/static/**", "/favicon.ico", "/v3/api-docs/**", "/swagger-ui/**");	            	        
	}	  
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    // Allow Vite React dev server as requested
	    configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173")); 
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
	    configuration.setAllowCredentials(true);
	    
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
	    return source;
	}
	
	
}