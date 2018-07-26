package com.bluehook.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ActivitiesController {

    @PostMapping(value = "/activities/simplify", produces = "application/vnd.galvanize.compact+json")
    @JsonView(Views.Compact.class)
    public List<FlattenedActivityResource> simplifyActivitiesCompact(@RequestBody ActivityResources activities) {
        return transformData(activities);
    }

    @PostMapping(value = "/activities/simplify", produces = "application/vnd.galvanize.detailed+json")
    @JsonView(Views.Detailed.class)
    public List<FlattenedActivityResource> simplifyActivities(@RequestBody ActivityResources activities) {
        return transformData(activities);
    }

    private List<FlattenedActivityResource> transformData(@RequestBody ActivityResources activities) {
        return activities.getActivities().stream().map(activity -> {
            int id = activity.getUser().getId();
            String user = activity.getUser().getUsername();
            String email = activity
                .getUser()
                .getEmails()
                .stream()
                .filter(e -> e.isPrimary())
                .findFirst()
                .get()
                .getAddress();
            String date = activity.getStatus().getDate();
            String statusText = activity.getStatus().getText();

            return new FlattenedActivityResource(
                id,
                user,
                email,
                date,
                statusText
            );

        }).collect(Collectors.toList());
    }

    static class Views {
        interface Compact {}
        interface Detailed extends Compact {}
    }

    static class FlattenedActivityResource {

        @JsonView(Views.Detailed.class)
        private final int userId;
        @JsonView(Views.Compact.class)
        private final String user;
        @JsonView(Views.Detailed.class)
        private final String email;
        @JsonView(Views.Compact.class)
        private final String date;
        @JsonView(Views.Compact.class)
        private final String statusText;

        public FlattenedActivityResource(int userId, String user, String email, String date, String statusText) {
            this.userId = userId;
            this.user = user;
            this.email = email;
            this.date = date;
            this.statusText = statusText;
        }

        public int getUserId() {
            return userId;
        }

        public String getUser() {
            return user;
        }

        public String getEmail() {
            return email;
        }

        public String getDate() {
            return date;
        }

        public String getStatusText() {
            return statusText;
        }
    }

    static class ActivityResources {
        private List<ActivityResource> activities;

        public List<ActivityResource> getActivities() {
            return activities;
        }

        public void setActivities(List<ActivityResource> activities) {
            this.activities = activities;
        }
    }

    static class ActivityResource {
        private UserResource user;
        private StatusResource status;

        public UserResource getUser() {
            return user;
        }

        public void setUser(UserResource user) {
            this.user = user;
        }

        public StatusResource getStatus() {
            return status;
        }

        public void setStatus(StatusResource status) {
            this.status = status;
        }
    }

    static class UserResource {
        private int id;
        private String username;
        private List<EmailResource> emails;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<EmailResource> getEmails() {
            return emails;
        }

        public void setEmails(List<EmailResource> emails) {
            this.emails = emails;
        }
    }

    static class StatusResource {
        private String text;
        private String date;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    static class EmailResource {
        private int id;
        private String address;
        private boolean primary;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean isPrimary() {
            return primary;
        }

        public void setPrimary(boolean primary) {
            this.primary = primary;
        }
    }

}