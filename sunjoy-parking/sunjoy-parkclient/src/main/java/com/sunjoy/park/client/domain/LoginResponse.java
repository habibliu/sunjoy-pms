package com.sunjoy.park.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/12/3
 */
@Data
public class LoginResponse {
    private int code;
    private String msg;
    private Data data;
    

    @Getter
    public static class Data {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private int expiresIn;

        // Getters and Setters

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }
    }
}