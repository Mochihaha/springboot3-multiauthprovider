package com.multiauthprovider.multiple_authentication_providers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SecurityAuthenticationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void givenCustomUsers_whenPingWithInvalidUser_thenRejected() {
        ResponseEntity<String> loginResult = makeRestCalltoPageThatRequiresAuthenticatedUser("user", "bad_credentials");

        assertThat(loginResult.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void givenMemUsers_whenGetPingWithValidMemUser_thenOk() {
        ResponseEntity<String> loginResult = makeRestCalltoPageThatRequiresAuthenticatedUser("memuser", "password");
        assertThat(loginResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResult.getBody()).isEqualTo("Hello from Local!");
    }

    @Test
    public void givenCustomUsers_whenGetPingWithValidPalindromeUser_thenOk() {
        ResponseEntity<String> loginResult = makeRestCalltoPageThatRequiresAuthenticatedUser("customUser", "mom");
        assertThat(loginResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResult.getBody()).isEqualTo("Hello from Local!");
    }

    private ResponseEntity<String> makeRestCalltoPageThatRequiresAuthenticatedUser(String username, String password) {
        return restTemplate.withBasicAuth(username,password)
                .getForEntity("/", String.class);
    }
}
