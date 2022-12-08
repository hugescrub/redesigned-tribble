package net.newsportal.controller;

import net.newsportal.payload.request.UserLoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Base64;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
class AuthControllerTest {

    private final MockMvc mvc;

    @Autowired
    AuthControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    void respond() throws Exception{
        UserLoginRequest creds = new UserLoginRequest("userOne", "somePasswd123");
        String credsEncoded = Base64.getEncoder().encodeToString((creds.getUsername() + ":" + creds.getPassword()).getBytes());
        mvc.perform(MockMvcRequestBuilders
                .post("/portal/auth/protected")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + credsEncoded)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}