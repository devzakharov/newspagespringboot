package ru.zrv.newspagespr.newspage.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class TagsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_return_tags() throws Exception {
        this.mockMvc.perform(get("/api/v1/tags?getalltags=1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}