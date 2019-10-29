package com.vepeter.example.log4j2spring.logger.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AuditEvent {

    private final String source;
    
    private final String name;
    
    private final String param;

    private final LocalDateTime eventDate;

    public AuditEvent(String source, String name, String param, LocalDateTime eventDate) {
        super();
        this.source = source;
        this.name = name;
        this.param = param;
        this.eventDate = eventDate;
    }
    
    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }
    
    public String getParam() {
        return param;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    public LocalDateTime getEventDate() {
        return eventDate;
    }

}
