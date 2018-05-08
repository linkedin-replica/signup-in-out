package com.linkedin.replica.signing.models;

import com.arangodb.entity.DocumentField;

public class LoggedInUser {
//    @DocumentField(DocumentField.Type.KEY)
    private String userId;
    private String name;
    private String profilePictureUrl;

    public LoggedInUser() {
        super();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getName() {
        return name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }
}
