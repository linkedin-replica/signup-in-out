package com.linkedin.replica.signUpInOut.models;

import com.arangodb.entity.DocumentField;

public class UserProfile {

    @DocumentField(DocumentField.Type.KEY)
    private String key;

    private String email;
    private String firstName;
    private String lastName;

    public UserProfile() {
        super();
    }

    public static UserProfile Instantiate(){
        return new UserProfile();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return "UserProfile {" +
                "key='" + key + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

}
