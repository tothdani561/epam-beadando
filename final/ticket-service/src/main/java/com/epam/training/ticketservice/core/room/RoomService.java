package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;

import java.util.List;

public interface RoomService {

    void createRoom(RoomDto roomDto);

    void updateRoom(String name, int rows, int cols);

    void deleteRoom(String name);

    List<RoomDto> getAllRooms();

}
