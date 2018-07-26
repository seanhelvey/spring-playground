package com.bluehook.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ActivitiesController.class)
public class ConversionTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testConversionWithDetails() throws Exception {
        RequestBuilder request = post("/activities/simplify")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Accept", "application/vnd.galvanize.detailed+json")
            .content(getJSON("/request.json"));

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$.[0].userId", equalTo(1467)))
            .andExpect(jsonPath("$.[0].user", equalTo("someuser")))
            .andExpect(jsonPath("$.[0].email", equalTo("personal@example.com")))
            .andExpect(jsonPath("$.[0].statusText", equalTo("Just went snowboarding today!")))
            .andExpect(jsonPath("$.[0].date", equalTo("2017-04-02 01:32")))
            .andExpect(jsonPath("$.[1].email", equalTo("otherprimary@example.com")));
    }

    @Test
    public void testConversionWithCompact() throws Exception {
        RequestBuilder request = post("/activities/simplify")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Accept", "application/vnd.galvanize.compact+json")
            .content(getJSON("/request.json"));

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$.[0].userId").doesNotExist())
            .andExpect(jsonPath("$.[0].user", equalTo("someuser")))
            .andExpect(jsonPath("$.[0].email").doesNotExist())
            .andExpect(jsonPath("$.[0].statusText", equalTo("Just went snowboarding today!")))
            .andExpect(jsonPath("$.[0].date", equalTo("2017-04-02 01:32")))
            .andExpect(jsonPath("$.[1].email").doesNotExist());
    }

    private String getJSON(String path) throws Exception {
        URL url = this.getClass().getResource(path);
        return new String(Files.readAllBytes(Paths.get(url.getFile())));
    }

}