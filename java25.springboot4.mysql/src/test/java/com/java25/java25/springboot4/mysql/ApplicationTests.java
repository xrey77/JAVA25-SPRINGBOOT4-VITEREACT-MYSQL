package com.java25.java25.springboot4.mysql;

import com.java25.java25.springboot4.mysql.dto.LoginDto;
import com.java25.java25.springboot4.mysql.entities.User;
import com.java25.java25.springboot4.mysql.repository.UserRepository;
import com.java25.java25.springboot4.mysql.services.JwtService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
// import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class ApplicationTests {
    @Autowired
    private RestTestClient restTestClient; 

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserService userService;

   @MockitoBean
    private JwtService jwtService;

    // @MockitoBean
    // private AuthService authService;

    @MockitoBean
    private KafkaProducerService kafkaProducerService;

    private LoginDto loginDto;
    private User mockUser;

  @BeforeEach
    void setUp() {
        loginDto = new LoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("password123");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword");
        mockUser.setFirstname("John");
        mockUser.setLastname("Doe");
        mockUser.setEmail("john@example.com");
    }    
  @Test
  void testSignupEndpoint() {
      User mockUser = new User();
      mockUser.setUsername("Jet");
      mockUser.setEmail("jet@lee.com");

      when(userRepository.save(any(User.class)))
              .thenReturn(mockUser);

      Map<String, String> signupData = Map.of(
              "firstname", "Jet",
              "lastname", "Lee",
              "email", "jet@lee.com",
              "mobile", "1234567890",
              "username", "Jet",
              "password", "rey"
      );

      doThrow(new RuntimeException("Kafka Broker Down"))
      .when(kafkaProducerService)
      .sendEvent(eq(KafkaProducerService.KafkaEventType.CREATE_USER), any(Map.class));

      this.restTestClient.post()
              .uri("/auth/signup")
              .contentType(MediaType.APPLICATION_JSON)
              .body(signupData) 
              .exchange()
              .expectStatus().isCreated()
              .expectBody()
              .jsonPath("$.message").isEqualTo("You have registered successfully, please login now.");
  }

    @Test
    void testLoginEndpoint() {
        User mockUser = new User();
        mockUser.setUsername("Rey");
        mockUser.setPassword("rey");

        when(userService.getUserName("Rey"))
                .thenReturn(mockUser);

        when(passwordEncoder.matches(eq("rey"), anyString()))
                .thenReturn(true);
                
        when(jwtService.generateToken2(any(User.class)))
                .thenReturn("mocked-jwt-token");

        doNothing()
                .when(kafkaProducerService)
                .sendEvent(eq(KafkaProducerService.KafkaEventType.SIGNIN_USER), any(Map.class));

        Map<String, String> signinData = Map.of(
            "username", "Rey",
            "password", "rey"
        );
        
        this.restTestClient.post()
                .uri("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .body(signinData)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("You have logged-in successfully.")
                .jsonPath("$.token").isEqualTo("mocked-jwt-token");
    }

     
  @Test
   void testGetUserIdEndpoint() {
       User mockUser = new User();
       mockUser.setId(1L);
       when(userService.getUser(1L)).thenReturn(mockUser);

        doThrow(new RuntimeException("Kafka Broker Down"))
        .when(kafkaProducerService)
        .sendEvent(eq(KafkaProducerService.KafkaEventType.GETUSER_BYID), any(Map.class));

       String mockToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJCQVJDTEFZUyBCQU5LIiwic3ViIjoicmV5QHlhaG9vLmNvbSIsInNjb3BlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzc2MDkxNDU1LCJleHAiOjE3NzYwOTIzNTV9.B-6nD0uDWnMI8MvCD335YWzT1Nb05bbfHMfdeI0am4k";

       this.restTestClient.get()
               .uri("/api/getuserid/1")
               .header("Authorization", "Bearer " + mockToken)                
               .accept(MediaType.APPLICATION_JSON)
               .exchange()
               .expectStatus().isBadRequest()
               .expectBody()
               .jsonPath("$.message").isEqualTo("Failed to send Kafka event")
               .jsonPath("$.error").isEqualTo("Kafka Broker Down");
   }    
}
