package com.rogeriofbrito.litmusgraphqljava.auth;

public class LoginResponse {

    public LoginResponse(String accessToken, Integer expiresIn, String projectID, String projectRole, String type) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.projectID = projectID;
        this.projectRole = projectRole;
        this.type = type;
    }

    private String accessToken;
    private Integer expiresIn;
    private String projectID;
    private String projectRole;
    private String type;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
