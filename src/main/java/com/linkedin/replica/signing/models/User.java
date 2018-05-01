package com.linkedin.replica.signing.models;

import com.arangodb.entity.DocumentField;
import com.arangodb.velocypack.annotations.Expose;

public class User {
    @DocumentField(DocumentField.Type.KEY)
    private String id;
    private String userId, email, firstName, lastName;

    @Expose(serialize = false, deserialize = false)
    private String password;

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User() {
    }
}