package com.edu.squashbot.telegram.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document("court_booking")
public class CourtBooking {
    @Id
    private String id;
    private LocalDateTime start;
    private LocalDateTime finish;
    private User user;
    private Court court;
}
