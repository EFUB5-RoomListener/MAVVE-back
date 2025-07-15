package com.efub.mavve.playlist.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddSongRequest {
    @NotBlank(message = "스포티파이 노래 아이디를 입력해주세요.")
    private final String spotifySongId;
}
