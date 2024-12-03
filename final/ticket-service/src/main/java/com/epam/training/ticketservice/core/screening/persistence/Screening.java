package com.epam.training.ticketservice.core.screening.persistence;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.room.persistence.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@IdClass(ScreeningId.class)
public class Screening {

    @Id
    @ManyToOne
    @JoinColumn(name = "movie_title", referencedColumnName = "title", nullable = false)
    private Movie movie;

    @Id
    @ManyToOne
    @JoinColumn(name = "room_name", referencedColumnName = "name", nullable = false)
    private Room room;

    @Id
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Screening(Movie movie, Room room, LocalDateTime startTime) {
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
    }
}

