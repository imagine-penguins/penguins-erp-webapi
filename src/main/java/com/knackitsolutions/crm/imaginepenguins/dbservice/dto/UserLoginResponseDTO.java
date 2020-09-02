package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

public class UserLoginResponseDTO {

    private String apiKey;

    private Long userId;

    private String responseMessage;

    public UserLoginResponseDTO() {

    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
