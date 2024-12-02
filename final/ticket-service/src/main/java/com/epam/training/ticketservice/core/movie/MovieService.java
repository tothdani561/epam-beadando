package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;

import java.util.List;

public interface MovieService {
    void createMovie(MovieDto movieDto);

    void updateMovie(String title, String genre, int duration);

    void deleteMovie(String title);

    List<MovieDto> getAllMovies();
}
