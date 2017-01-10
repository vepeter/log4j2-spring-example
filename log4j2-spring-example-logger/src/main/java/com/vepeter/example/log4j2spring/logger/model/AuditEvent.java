package com.vepeter.example.log4j2spring.logger.model;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AuditEvent {

    private final String name;

    private final LocalDateTime eventDate;

    public AuditEvent(String name, LocalDateTime eventDate) {
        super();
        this.name = name;
        this.eventDate = eventDate;
    }

    public String getName() {
        return name;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    public LocalDateTime getEventDate() {
        return eventDate;
    }

}
