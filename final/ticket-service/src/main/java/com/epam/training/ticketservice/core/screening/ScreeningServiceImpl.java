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
import java.util.Comparator;
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

        // Ellenőrizzük, hogy a film és a terem létezik
        var movieOptional = movieService.getAllMovies().stream()
                .filter(movie -> movie.title().equals(movieTitle))
                .findFirst();

        var roomOptional = roomService.getAllRooms().stream()
                .filter(room -> room.roomName().equals(roomName))
                .findFirst();

        if (movieOptional.isEmpty()) {
            throw new IllegalArgumentException("Movie with title '" + movieTitle + "' does not exist");
        }

        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with name '" + roomName + "' does not exist");
        }

        // Vetítés időtartamának kiszámítása
        int movieDuration = movieOptional.get().duration();
        LocalDateTime endTime = startTime.plusMinutes(movieDuration);

        // Ütközés ellenőrzése
        boolean hasOverlap = screeningRepository.findAll().stream()
                .filter(screening -> screening.getRoom().getRoomName().equals(roomName)) // Csak az adott terem
                .anyMatch(screening -> {
                    LocalDateTime existingStart = screening.getStartTime();
                    LocalDateTime existingEnd = existingStart.plusMinutes(screening.getMovie().getDuration());
                    return startTime.isBefore(existingEnd) && endTime.isAfter(existingStart);
                });

        if (hasOverlap) {
            throw new IllegalArgumentException("There is an overlapping screening");
        }

        // 10 perces szünet ellenőrzése
        boolean isInBreakPeriod = screeningRepository.findAll().stream()
                .filter(screening -> screening.getRoom().getRoomName().equals(roomName)) // Csak az adott terem
                .anyMatch(screening -> {
                    LocalDateTime existingEnd = screening.getStartTime().plusMinutes(screening.getMovie().getDuration());
                    LocalDateTime breakEnd = existingEnd.plusMinutes(10); // 10 perces szünet vége
                    return startTime.isBefore(breakEnd) && startTime.isAfter(existingEnd);
                });

        if (isInBreakPeriod) {
            throw new IllegalArgumentException("This would start in the break period after another screening in this room");
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
                .sorted(Comparator.comparing(Screening::getCreatedAt)) // Hozzáadás sorrendje
                .map(screening -> new ScreeningDto(
                        screening.getMovie().getTitle(),
                        screening.getRoom().getRoomName(),
                        screening.getStartTime()
                ))
                .toList();
    }
}
