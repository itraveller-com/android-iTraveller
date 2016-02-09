package com.itraveller.moxtraChat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jay on 08/12/2015.
 */
public class CreateBinderResponse {
    @JsonProperty("code")
    private String code;
    @SerializedName("data")
    private BinderData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BinderData getData() {
        return data;
    }

    public void setData(BinderData data) {
        this.data = data;
    }

    public class BinderData{

        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("created_time")
        private String creaetd_time;
        @JsonProperty("updated_time")
        private String updated_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreaetd_time() {
            return creaetd_time;
        }

        public void setCreaetd_time(String creaetd_time) {
            this.creaetd_time = creaetd_time;
        }

        public String getUpdated_time() {
            return updated_time;
        }

        public void setUpdated_time(String updated_time) {
            this.updated_time = updated_time;
        }
    }
}
