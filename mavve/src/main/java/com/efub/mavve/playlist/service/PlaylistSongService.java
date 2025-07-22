package com.efub.mavve.playlist.service;

import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.playlist.domain.PlaylistSong;

import com.efub.mavve.songs.domain.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaylistSongService {

    public PlaylistSong getPlaylistSongByPlaylistAndSong(Playlist playlist, Song song){
        return playlist.getPlaylistSongs().stream()
                .filter(ps -> ps.getSong().equals(song)).findFirst()
                .orElseThrow(()-> new MavveException(ExceptionCode.SONG_NOT_IN_PLAYLIST));
    }
}
