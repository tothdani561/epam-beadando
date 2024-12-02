package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private final RoomService roomService;

    @Override
    public void createScreening(ScreeningDto screeningDto) {
        String movieTitle = screeningDto.movieTitle();
        String roomName = screeningDto.roomName();
        LocalDateTime startTime = screeningDto.startTime();

        // Film létezésének ellenőrzése
        var movieOptional = movieService.getAllMovies()
                .stream()
                .filter(movie -> movie.title().equals(movieTitle))
                .findFirst();

        if (movieOptional.isEmpty()) {
            throw new IllegalArgumentException("Movie with title '" + movieTitle + "' does not exist");
        }

        // Terem létezésének ellenőrzése
        var roomOptional = roomService.getAllRooms()
                .stream()
                .filter(room -> room.roomName().equals(roomName))
                .findFirst();

        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with name '" + roomName + "' does not exist");
        }

        // Lekérjük a film hosszát
        int movieDuration = movieOptional.get().duration();

        // Vetítés időtartamának kiszámítása
        LocalDateTime endTime = startTime.plusMinutes(movieDuration);
        LocalDateTime breakEndTime = endTime.plusMinutes(10);

        // Ütközés ellenőrzése
        List<Screening> overlappingScreenings = screeningRepository.findAll()
                .stream()
                .filter(screening -> screening.getRoom().getRoomName().equals(roomName))
                .filter(screening -> {
                    LocalDateTime screeningEnd = screening.getStartTime()
                            .plusMinutes(screening.getMovie().getDuration());
                    LocalDateTime screeningBreakEnd = screeningEnd.plusMinutes(10);

                    return !(endTime.isBefore(screening.getStartTime()) || breakEndTime.isBefore(screening.getStartTime())) &&
                            !(startTime.isAfter(screeningBreakEnd));
                })
                .toList();

        if (!overlappingScreenings.isEmpty()) {
            throw new IllegalArgumentException("There is an overlapping screening");
        }

        // Vetítés létrehozása
        Screening screening = new Screening(
                Movie.fromDto(movieOptional.get()),
                Room.fromDto(roomOptional.get()),
                startTime
        );
        screeningRepository.save(screening);
    }

    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
        String movieTitle = screeningDto.movieTitle();
        String roomName = screeningDto.roomName();
        LocalDateTime startTime = screeningDto.startTime();

        Screening screening = screeningRepository.findAll()
                .stream()
                .filter(s -> s.getMovie().getTitle().equals(movieTitle)
                        && s.getRoom().getRoomName().equals(roomName)
                        && s.getStartTime().equals(startTime))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Screening does not exist"));

        screeningRepository.delete(screening);
    }

    @Override
    public List<ScreeningDto> listScreenings() {
        return screeningRepository.findAll().stream()
                .map(screening -> new ScreeningDto(
                        screening.getMovie().getTitle(),
                        screening.getRoom().getRoomName(),
                        screening.getStartTime()
                ))
                .toList();
    }
}
