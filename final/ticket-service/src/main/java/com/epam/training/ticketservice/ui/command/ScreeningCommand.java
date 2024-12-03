package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {

    private final ScreeningService screeningService;
    private final UserService userService;
    private final MovieService movieService;

    @ShellMethod(key = "create screening", value = "Create a new screening")
    public String createScreening(String movieTitle, String roomName, String startTimeStr) {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }

        try {
            LocalDateTime startTime = LocalDateTime.parse(
                    startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            ScreeningDto screeningDto = new ScreeningDto(movieTitle, roomName, startTime);

            screeningService.createScreening(screeningDto);
            return "Screening created successfully!";
        } catch (DateTimeParseException e) {
            return "Invalid date format. Please use 'YYYY-MM-DD HH:mm'.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "delete screening", value = "Delete an existing screening")
    public String deleteScreening(String movieTitle, String roomName, String startTimeStr) {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }

        try {
            LocalDateTime startTime = LocalDateTime.parse(
                    startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            ScreeningDto screeningDto = new ScreeningDto(movieTitle, roomName, startTime);

            screeningService.deleteScreening(screeningDto);
            return "Screening deleted successfully!";
        } catch (DateTimeParseException e) {
            return "Invalid date format. Please use 'YYYY-MM-DD HH:mm'.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "list screenings", value = "List all screenings")
    public String listScreenings() {
        List<ScreeningDto> screenings = screeningService.listScreenings();

        if (screenings.isEmpty()) {
            return "There are no screenings";
        }

        return screenings.stream()
                .map(screening -> {
                    String movieTitle = screening.movieTitle();
                    String roomName = screening.roomName();
                    LocalDateTime startTime = screening.startTime();

                    // Adatok kinyerése az entitásokból
                    var movieOptional = movieService.getAllMovies().stream()
                            .filter(movie -> movie.title().equals(movieTitle))
                            .findFirst();

                    if (movieOptional.isEmpty()) {
                        return "Invalid screening data (missing movie)";
                    }

                    var movie = movieOptional.get();
                    return String.format(
                            "%s (%s, %d minutes), screened in room %s, at %s",
                            movie.title(),
                            movie.genre(),
                            movie.duration(),
                            roomName,
                            startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    );
                })
                .collect(Collectors.joining("\n"));
    }
}
