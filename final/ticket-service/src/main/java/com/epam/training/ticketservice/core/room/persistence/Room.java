package com.epam.training.ticketservice.core.room.persistence;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @Column(name = "name", nullable = false)
    private String roomName;

    @Column(name = "row_count") // Megváltoztatott oszlopnév
    private Integer rows;
    private int cols;

    public static Room fromDto(RoomDto dto) {
        return new Room(dto.roomName(), dto.rows(), dto.cols());
    }

}
