package com.epam.training.ticketservice.core.screening.persistence;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.room.persistence.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}

