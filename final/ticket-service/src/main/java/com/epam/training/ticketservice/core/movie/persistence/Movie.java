package com.epam.training.ticketservice.core.movie.persistence;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    private String title;
    private String genre;
    private int duration;

    public static Movie fromDto(MovieDto dto) {
        return new Movie(dto.title(), dto.genre(), dto.duration());
    }

}
