package com.project.together.config.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginFormTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("member_id", "together3")
                        .param("member_pw", "11111"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 로그인 성공 시 처리할 로직을 추가하세요
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));  // 로그인 성공 후 리다이렉트 될 URL
    }
}