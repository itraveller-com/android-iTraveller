package com.itraveller.moxtraChat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jay on 07/12/2015.
 */
public class AccessTokenModel {

    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("token_type")
    private String token_type;

    @JsonProperty("expires_in")
    private String expires_in;
    @JsonProperty("scope")
    private String scope;

    public String getAccesstoken() {
        return access_token;
    }

    public void setAccesstoken(String access_token) {
        this.access_token = access_token;
    }

    public String getTokentype() {
        return token_type;
    }

    public void setTokentype(String tokentype) {
        this.token_type = token_type;
    }


    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
