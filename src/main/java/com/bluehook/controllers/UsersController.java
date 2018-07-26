package com.bluehook.controllers;

import com.bluehook.models.User;
import com.bluehook.models.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserRepository repo;

    public UsersController(UserRepository repo) {
        this.repo = repo;
    }

    @GetMapping("")
    public UserListResource list() {
        return new UserListResource(this.repo.findAll());
    }

    @PostMapping("")
    public User create(@RequestBody UserPayload payload) {
        return this.repo.save(payload.getUser());
    }

    @GetMapping("/{id}")
    public UserPayload show(@PathVariable Long id) throws Exception {
        return new UserPayload(this.repo.findById(id)
            .orElseThrow(() -> new Exception()));
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return this.repo.save(user);
    }

    @DeleteMapping("/{id}")
    public UserDeleteResponse delete(@PathVariable Long id) {
        this.repo.deleteById(id);
        return new UserDeleteResponse(this.repo.count());
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody User user) {
        User dbUser = this.repo.findByEmail(user.getEmail());
        if (dbUser != null && dbUser.getPasswordDigest().equals(user.getPasswordDigest())) {
            return new AuthenticationResponse(dbUser);
        } else {
            return new AuthenticationResponse();
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class AuthenticationResponse {
        private final User user;
        private final boolean authenticated;

        public AuthenticationResponse(User user) {
            this.user = user;
            this.authenticated = true;
        }

        public AuthenticationResponse() {
            this.user = null;
            this.authenticated = false;
        }

        public User getUser() {
            return user;
        }

        public boolean isAuthenticated() {
            return authenticated;
        }
    }

    static class UserListResource {
        private List<User> users;

        public UserListResource(List<User> users) {
            this.users = users;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    static class UserPayload {
        private User user;

        public UserPayload(User user) {
            this.user = user;
        }

        public UserPayload() {
        }


        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    static class UserDeleteResponse {
        private final Long count;

        UserDeleteResponse(Long count) {
            this.count = count;
        }

        public Long getCount() {
            return count;
        }
    }

}
