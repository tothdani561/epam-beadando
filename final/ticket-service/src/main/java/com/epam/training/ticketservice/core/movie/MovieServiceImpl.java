package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;


    @Override
    public void createMovie(MovieDto movieDto) {
        if (movieRepository.existsById(movieDto.title())) {
            throw new IllegalArgumentException("Movie with title '" + movieDto.title() + "' already exists");
        }

        Movie movie = new Movie(movieDto.title(), movieDto.genre(), movieDto.duration());
        movieRepository.save(movie);
    }
}
