package com.edu.squashbot.telegram.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Court {
    @Id
    private String id;
    private String name;
}
