package com.epam.training.ticketservice.core.screening.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningId implements Serializable {

    private String movie; // Film címe
    private String room; // Terem neve
    private LocalDateTime startTime; // Vetítés kezdési időpontja
}
