package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {

    private final MovieService movieService;
    private final UserService userService;

    @ShellMethod(key = "create movie", value = "Create a new movie")
    public String createMovie(String title, String genre, int duration) {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }
        try {
            MovieDto movieDto = new MovieDto(title, genre, duration);
            movieService.createMovie(movieDto);
            return "Movie '" + title + "' created successfully!";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "update movie", value = "Update an existing movie")
    public String updateMovie(String title, String genre, int duration) {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }
        try {
            movieService.updateMovie(title, genre, duration);
            return "Movie '" + title + "' updated successfully!";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "list movies", value = "List all movies")
    public String listMovies() {
        List<MovieDto> movies = movieService.getAllMovies();
        if (movies.isEmpty()) {
            return "There are no movies at the moment";
        }
        return movies.stream()
                .map(movie -> String.format("%s (%s, %d minutes)", movie.title(), movie.genre(), movie.duration()))
                .collect(Collectors.joining("\n"));
    }


    @ShellMethod(key = "delete movie", value = "Delete an existing movie")
    public String deleteMovie(String title) {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }
        try {
            movieService.deleteMovie(title);
            return "Movie '" + title + "' deleted successfully!";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
