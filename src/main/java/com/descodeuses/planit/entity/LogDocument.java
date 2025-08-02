package com.descodeuses.planit.entity;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "LogDocument")
public class LogDocument {
    @Id
    private String  id;
    private String text;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Map<String, Object> extras = new HashMap<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime Timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object>extras) {
        this.extras = extras;
    }

}

