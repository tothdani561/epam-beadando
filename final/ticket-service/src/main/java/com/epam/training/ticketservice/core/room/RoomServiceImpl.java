package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(RoomDto roomDto) {
        if (roomRepository.existsById(roomDto.roomName())) {
            throw new IllegalArgumentException("Room with name '" + roomDto.roomName() + "' already exists");
        }

        Room room = new Room(roomDto.roomName(), roomDto.rows(), roomDto.cols());
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(String name, int rows, int columns) {
        Room room = roomRepository.findById(name)
                .orElseThrow(() -> new IllegalArgumentException("Room with name '" + name + "' does not exist"));

        room.setRows(rows);
        room.setCols(columns);
        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(String name) {
        if (!roomRepository.existsById(name)) {
            throw new IllegalArgumentException("Room with name '" + name + "' does not exist");
        }
        roomRepository.deleteById(name);
    }

    @Override
    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(room -> new RoomDto(room.getRoomName(), room.getRows(), room.getCols()))
                .toList();
    }
}
