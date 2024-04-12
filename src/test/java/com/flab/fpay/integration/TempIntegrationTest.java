package com.flab.fpay.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO: 2024/04/02 초기 세팅 구성 후 삭제
public class TempIntegrationTest extends IntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("성공 응답")
    public void successTest() throws Exception {
        mockMvc.perform(get("/v1/test/success")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());

    }

    @Test
    @DisplayName("실패 응답")
    public void failTest() throws Exception {
        mockMvc.perform(get("/v1/test/fail")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("커스텀 메세지 실패 응답")
    public void failWithMessageTest() throws Exception {
        mockMvc.perform(get("/v1/test/fail/message")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("내부 서버 오류 응답")
    public void internalFailTest() throws Exception {
        mockMvc.perform(get("/v1/test/fail/internal")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
    }
}
