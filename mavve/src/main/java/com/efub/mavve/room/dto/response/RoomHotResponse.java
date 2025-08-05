package com.efub.mavve.room.dto.response;

import com.efub.mavve.room.dto.summary.RoomHotSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RoomHotResponse {
    private List<RoomHotSummary> rooms;
}
