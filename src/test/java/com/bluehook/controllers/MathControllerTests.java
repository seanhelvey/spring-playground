package com.bluehook.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MathController.class)
public class MathControllerTests {

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldReturnPi() throws Exception {

    }

    @Test
    public void shouldAdd() throws Exception {
        this.mvc.perform(get("/math/calculate?operation=add&x=7&y=3"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    public void shouldSubtract() throws Exception {
        this.mvc.perform(get("/math/calculate?operation=subtract&x=7&y=3"))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));
    }

    @Test
    public void shouldMultiply() throws Exception {
        this.mvc.perform(get("/math/calculate?operation=multiply&x=7&y=3"))
                .andExpect(status().isOk())
                .andExpect(content().string("21"));
    }

    @Test
    public void shouldDivide() throws Exception {
        this.mvc.perform(get("/math/calculate?operation=divide&x=9&y=3"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    public void shouldSum() throws Exception {
        this.mvc.perform(get("/math/sum?n=1&m=2"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }


}


