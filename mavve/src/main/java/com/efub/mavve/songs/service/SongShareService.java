package com.efub.mavve.songs.service;

import com.efub.mavve.artist.service.ArtistService;
import com.efub.mavve.auth.domain.User;
import com.efub.mavve.auth.service.jwt.SpotifyTokenService;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.artist.domain.Artist;
import com.efub.mavve.songs.domain.Song;
import com.efub.mavve.songs.dto.response.spotify.SongInfoResponse;
import com.efub.mavve.songs.repository.SongRepository;
import com.efub.mavve.songs.service.spotify.SpotifyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SongShareService {

    private final SongRepository songRepository;
    private final SpotifyClient spotifyClient;
    private final SpotifyTokenService spotifyTokenService;
    private final ArtistService artistService;

    @Transactional(readOnly = true)
    public boolean existSongBySpotifySongId(String spotifySongId) {
        return songRepository.existsBySpotifySongId(spotifySongId);
    }

    @Transactional
    public void saveSong(Song song) {
        songRepository.save(song);
    }

    @Transactional(readOnly = true)
    public Song getSongBySpotifySongId(String spotifySongId) {
        return songRepository.findBySpotifySongId(spotifySongId).orElseThrow(()-> new MavveException(ExceptionCode.SONG_NOT_FOUND));
    }

    @Transactional
    public Song getSongBySongId(Long songId) {
        return songRepository.findBySongId(songId)
                .orElseThrow(()-> new MavveException(ExceptionCode.SONG_NOT_FOUND));
    }

    @Transactional
    public Song saveSongBySpotifySongId(String spotifySongId, User user) {
        String userId = user.getUserId().toString();
        String accessToken = spotifyTokenService.getAccessToken(userId);
        SongInfoResponse songInfoResponse = spotifyClient.getSongInfo(spotifySongId, accessToken);
        Song newSong = Song.fromSongInfoResponse(songInfoResponse);
        saveSong(newSong);
        List<Artist> artists = artistService.findOrCreateArtists(songInfoResponse.getArtists());
        newSong.addArtists(artists);
        return newSong;
    }
}
