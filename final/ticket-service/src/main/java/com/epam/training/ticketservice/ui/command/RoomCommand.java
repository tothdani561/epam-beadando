package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class RoomCommand {

    private final RoomService roomService;
    private final UserService userService;

    @ShellMethod(key = "list rooms", value = "List all rooms")
    public String listRooms() {
        List<RoomDto> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            return "There are no rooms at the moment";
        }
        return rooms.stream()
                .map(room -> String.format(
                        "Room %s with %d seats, %d rows and %d columns",
                        room.roomName(),
                        room.rows() * room.cols(),
                        room.rows(),
                        room.cols()
                ))
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(key = "create room", value = "Create a new room")
    public String createRoom(String name, int rows, int columns) {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }

        try {
            RoomDto roomDto = new RoomDto(name, rows, columns);
            roomService.createRoom(roomDto);
            return "Room '" + name + "' created successfully!";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "update room", value = "Update an existing room")
    public String updateRoom(String name, int rows, int columns) {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }

        try {
            roomService.updateRoom(name, rows, columns);
            return "Room '" + name + "' updated successfully!";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(key = "delete room", value = "Delete an existing room")
    public String deleteRoom(String name) {
        if (!userService.isAdminLoggedIn()) {
            return "Access denied. Please sign in as an admin.";
        }

        try {
            roomService.deleteRoom(name);
            return "Room '" + name + "' deleted successfully!";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
