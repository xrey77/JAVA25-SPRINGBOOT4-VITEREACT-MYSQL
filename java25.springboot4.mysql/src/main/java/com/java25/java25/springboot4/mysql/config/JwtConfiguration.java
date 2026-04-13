//package com.java25.java25.springboot4.mysql.config;
//
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//
//import io.jsonwebtoken.security.KeyPair;
//
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//
//@Configuration
//public class JwtConfiguration {
//
//	@Bean
//	public KeyPair keyPair() throws NoSuchAlgorithmException {
//	    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//	    generator.initialize(2048);
//	    return generator.generateKeyPair();
//	}
//	
//	@Bean
//	public JwtEncoder jwtEncoder(KeyPair keyPair) {
//	    // New simplified builder in Spring Security 7.0+
//	    return NimbusJwtEncoder.withKeyPair(
//	        (RSAPublicKey) keyPair.getPublic(), 
//	        (RSAPrivateKey) keyPair.getPrivate()
//	    ).build();
//	}	
//
//}
