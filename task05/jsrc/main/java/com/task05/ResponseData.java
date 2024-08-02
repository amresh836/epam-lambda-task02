package com.task05;

import lombok.Builder;

@Builder
public class ResponseData {
    private int statusCode;
    private EventData eventData;

    public ResponseData() {
    }

    public ResponseData(int statusCode, EventData eventData) {
        this.statusCode = statusCode;
        this.eventData = eventData;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }
}
