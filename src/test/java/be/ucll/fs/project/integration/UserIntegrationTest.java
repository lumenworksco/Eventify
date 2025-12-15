package be.ucll.fs.project.integration;

import be.ucll.fs.project.dto.LoginRequest;
import be.ucll.fs.project.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testLoginAndAccessProtectedEndpoint() {
        // Step 1: Login to get JWT token
        LoginRequest loginRequest = new LoginRequest("Alice Johnson", "password123");
        
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                "/api/users/login",
                loginRequest,
                LoginResponse.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String token = loginResponse.getBody().getToken();
        assertNotNull(token);
        assertEquals("ADMIN", loginResponse.getBody().getRole());

        // Step 2: Access protected endpoint with token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> usersResponse = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, usersResponse.getStatusCode());
        assertNotNull(usersResponse.getBody());
        assertTrue(usersResponse.getBody().contains("Alice Johnson"));
    }

    @Test
    void testAccessProtectedEndpointWithoutToken() {
        // Try to access protected endpoint without token
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/users/1",
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testDifferentRolesBehavior() {
        // Test ADMIN role
        LoginRequest adminLogin = new LoginRequest("Alice Johnson", "password123");
        ResponseEntity<LoginResponse> adminResponse = restTemplate.postForEntity(
                "/api/users/login",
                adminLogin,
                LoginResponse.class
        );
        
        String adminToken = adminResponse.getBody().getToken();
        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setBearerAuth(adminToken);

        ResponseEntity<String> adminUsersResponse = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                new HttpEntity<>(adminHeaders),
                String.class
        );

        // Admin should see full details (contains password field in response structure)
        assertEquals(HttpStatus.OK, adminUsersResponse.getStatusCode());
        assertTrue(adminUsersResponse.getBody().contains("password"));

        // Test USER role
        LoginRequest userLogin = new LoginRequest("Charlie Brown", "password123");
        ResponseEntity<LoginResponse> userResponse = restTemplate.postForEntity(
                "/api/users/login",
                userLogin,
                LoginResponse.class
        );
        
        String userToken = userResponse.getBody().getToken();
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);

        ResponseEntity<String> userUsersResponse = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                new HttpEntity<>(userHeaders),
                String.class
        );

        // Regular user should see limited details (no password field)
        assertEquals(HttpStatus.OK, userUsersResponse.getStatusCode());
        assertFalse(userUsersResponse.getBody().contains("\"password\""));
    }
}
