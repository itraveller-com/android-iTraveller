package com.itraveller.moxtraChat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jay on 08/12/2015.
 */
public class AddUserResponse {
    @JsonProperty("code")
    public String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
