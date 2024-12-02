package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void updateMovie(String title, String genre, int duration) {
        Movie movie = movieRepository.findById(title)
                .orElseThrow(() -> new IllegalArgumentException("Movie with title '" + title + "' does not exist"));

        movie.setGenre(genre);
        movie.setDuration(duration);
        movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(String title) {
        if (!movieRepository.existsById(title)) {
            throw new IllegalArgumentException("Movie with title '" + title + "' does not exist");
        }
        movieRepository.deleteById(title);
    }

    @Override
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movie -> new MovieDto(movie.getTitle(), movie.getGenre(), movie.getDuration()))
                .toList();
    }
}
