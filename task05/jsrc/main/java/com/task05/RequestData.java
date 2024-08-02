package com.task05;

import lombok.Builder;

import java.util.Map;

@Builder
public class RequestData {

    private int principalId;
    private Map<String, String> content;


    public RequestData(int principalId, Map<String, String> content) {
        this.principalId = principalId;
        this.content = content;
    }

    public RequestData() {
    }

    public int getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(int principalId) {
        this.principalId = principalId;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }
}
