package com.efub.mavve.artist.service;

import com.efub.mavve.artist.domain.Artist;
import com.efub.mavve.artist.repository.ArtistRepository;
import com.efub.mavve.songs.dto.response.spotify.SongInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;

    public List<Artist> findOrCreateArtists(List<SongInfoResponse.Artist> artistResponses) {
        List<Artist> artists = new ArrayList<>();
        for (SongInfoResponse.Artist artistDto : artistResponses) {
            String spotifyArtistId = artistDto.getId();
            Artist artist = artistRepository.findByArtistSpotifyId(spotifyArtistId)
                    .orElseGet(() -> {
                        Artist newArtist = Artist.create(artistDto.getName(), spotifyArtistId);
                        return artistRepository.save(newArtist);
                    });
            artists.add(artist);
        }
        return artists;
    }

}
