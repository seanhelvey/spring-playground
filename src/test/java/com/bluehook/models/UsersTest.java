package com.bluehook.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsersTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository repo;

    @Test
    @Transactional
    @Rollback
    public void testIndex() throws Exception {
        User user = new User();
        user.setEmail("user@example.com");
        this.repo.save(user);

        this.mvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.users[0].id", equalTo(user.getId().intValue())))
            .andExpect(jsonPath("$.users[0].email", equalTo(user.getEmail())))
            .andExpect(jsonPath("$.users[0].password").doesNotExist())
            .andExpect(jsonPath("$.users[0].passwordDigest").doesNotExist());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreate() throws Exception {
        RequestBuilder request = post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"user\": {\"email\": \"user@example.com\", \"password\": \"foo\"}}");

        this.mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.email", equalTo("user@example.com")))
            .andExpect(jsonPath("$.password").doesNotExist())
            .andExpect(jsonPath("$.passwordDigest").doesNotExist());

        assertThat(this.repo.count(), equalTo(1L));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateWithPassword() throws Exception {
        User user = new User();
        user.setEmail("user@example.com");
        this.repo.save(user);

        RequestBuilder request = patch("/users/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\": " + user.getId().toString() + ", \"email\": \"other@example.com\", \"password\": \"foo\"}");

        this.mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(user.getId().intValue())))
            .andExpect(jsonPath("$.email", equalTo("other@example.com")))
            .andExpect(jsonPath("$.password").doesNotExist())
            .andExpect(jsonPath("$.passwordDigest").doesNotExist());

        assertThat(this.repo.count(), equalTo(1L));
    }

//    @Test
//    @Transactional
//    @Rollback
//    public void testShow() throws Exception {
//        User user = new User();
//        user.setEmail("user@example.com");
//        this.repo.save(user);
//
//        this.mvc.perform(get("/users/{id}", user.getId()))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id", equalTo(user.getId().intValue())))
//            .andExpect(jsonPath("$.email", equalTo(user.getEmail())))
//            .andExpect(jsonPath("$.password").doesNotExist())
//            .andExpect(jsonPath("$.passwordDigest").doesNotExist());
//    }

    @Test
    @Transactional
    @Rollback
    public void testDelete() throws Exception {
        User user = new User();
        user.setEmail("user@example.com");
        this.repo.save(user);

        this.mvc.perform(delete("/users/{id}", user.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count", equalTo(0)));

        assertThat(this.repo.count(), equalTo(0L));
    }

    @Test
    @Transactional
    @Rollback
    public void testAuthenticateWithValidEmail() throws Exception {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("foo");
        this.repo.save(user);

        RequestBuilder request = post("/users/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\": \"user@example.com\", \"password\": \"foo\"}");

        this.mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.authenticated", equalTo(true)))
            .andExpect(jsonPath("$.user.id", equalTo(user.getId().intValue())))
            .andExpect(jsonPath("$.user.email", equalTo("user@example.com")))
            .andExpect(jsonPath("$.user.password").doesNotExist())
            .andExpect(jsonPath("$.user.passwordDigest").doesNotExist());
    }

    @Test
    @Transactional
    @Rollback
    public void testAuthenticateWithInvalidEmail() throws Exception {
        RequestBuilder request = post("/users/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\": \"user@example.com\", \"password\": \"foo\"}");

        this.mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.authenticated", equalTo(false)))
            .andExpect(jsonPath("$.user").doesNotExist());
    }

    @Test
    @Transactional
    @Rollback
    public void testAuthenticateWithInvalidPassword() throws Exception {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("foo");
        this.repo.save(user);

        RequestBuilder request = post("/users/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\": \"user@example.com\", \"password\": \"bar\"}");

        this.mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.authenticated", equalTo(false)))
            .andExpect(jsonPath("$.user").doesNotExist());
    }


}
