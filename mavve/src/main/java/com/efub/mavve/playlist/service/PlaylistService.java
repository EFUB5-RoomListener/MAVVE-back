package com.efub.mavve.playlist.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.playlist.domain.Playlist;
import com.efub.mavve.playlist.domain.PlaylistSong;
import com.efub.mavve.playlist.dto.request.AddSongRequest;
import com.efub.mavve.playlist.dto.request.PlaylistCreateRequest;
import com.efub.mavve.playlist.dto.request.PlaylistUpdateRequest;
import com.efub.mavve.playlist.dto.response.PlaylistListResponse;
import com.efub.mavve.playlist.dto.response.PlaylistResponse;
import com.efub.mavve.playlist.dto.summary.PlaylistSummary;
import com.efub.mavve.playlist.repository.PlaylistRepository;
import com.efub.mavve.songs.domain.Song;
import com.efub.mavve.songs.dto.response.SongResponse;
import com.efub.mavve.songs.service.SongShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.efub.mavve.songs.service.spotify.SearchType.playlist;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongShareService songShareService;
    private final PlaylistSongService playlistSongService;

    // 플레이리스트 생성
    @Transactional
    public Playlist createPlaylist(PlaylistCreateRequest request, User user) {

        String name = request.name();
        String playImageUrl = request.playImageUrl();

        // 플레이리스트 이름 중복 검사
        if (playlistRepository.existsByName(name)) {
            throw new MavveException(ExceptionCode.TITLE_ALREADY_EXIST);
        }

        Playlist newPlaylist = request.toEntity(user);
        return playlistRepository.save(newPlaylist);
    }

    // 플레이리스트 목록 조회
    @Transactional(readOnly = true)
    public PlaylistListResponse getAllPlaylists() {
        List<PlaylistSummary> playlistSummaries = playlistRepository.findByOrderByPlaylistIdDesc().stream()
                .map(PlaylistSummary::from).toList();
        return new PlaylistListResponse(playlistSummaries);
    }

    // 플레이리스트 상세 조회
    @Transactional(readOnly = true)
    public PlaylistResponse getPlaylist(Long playlistId) {

        Playlist playlist = getPlaylistOrThrow(playlistId);
        return PlaylistResponse.from(playlist);
    }

    // 플레이리스트 수정
    @Transactional
    public void updatePlaylist(Long playlistId, PlaylistUpdateRequest request, User user) {

        String name = request.name();
        String playImageUrl = request.playImageUrl();

        // 플레이리스트 이름 중복 검사
        if (playlistRepository.existsByName(name)) {
            throw new MavveException(ExceptionCode.TITLE_ALREADY_EXIST);
        }

        Playlist playlist = getPlaylistOrThrow(playlistId);
        validatePlaylistOwner(playlist, user);
        playlist.changePlaylist(name, playImageUrl);
    }

    // 플레이리스트 삭제
    @Transactional
    public void deletePlaylist(Long playlistId, User user) {

        Playlist playlist = getPlaylistOrThrow(playlistId);
        validatePlaylistOwner(playlist, user);
        playlistRepository.delete(playlist);
    }

    // 플레이리스트 검색
    @Transactional(readOnly = true)
    public List<PlaylistSummary> searchPlaylists(String keyword, User user) {
        List<Playlist> playlists = playlistRepository.findByUserAndNameContainingIgnoreCaseOrderByCreatedAtDesc(user, keyword);

        return playlists.stream()
                .map(PlaylistSummary::from)
                .collect(Collectors.toList());
    }

    //플레이리스트에 노래 추가
    @Transactional
    public SongResponse addSongInPlaylist(User user, Long  playlistId, AddSongRequest addSongRequest){
        Playlist playlist = getPlaylistOrThrow(playlistId);
        validatePlaylistOwner(playlist, user);
        String spotifySongId = addSongRequest.getSpotifySongId();
        // db에 이미 있는 노래인지 확인
        Song newSong = songShareService.existSongBySpotifySongId(spotifySongId)
                ? songShareService.getSongBySpotifySongId(spotifySongId)
                : songShareService.saveSongBySpotifySongId(spotifySongId, user);
        // 저장된 노래를 playlist에 추가
        playlist.addSong(newSong);
        // 해당 노래에 대한 정보를 return
        return SongResponse.fromSongEntity(newSong);
    }

    @Transactional
    public void deleteSongInPlaylist(User user, Long playlistId, String spotifySongId) {
        Playlist playlist = getPlaylistOrThrow(playlistId);
        validatePlaylistOwner(playlist, user);
        Song song = songShareService.getSongBySpotifySongId(spotifySongId);
        PlaylistSong target = playlistSongService.getPlaylistSongByPlaylistAndSong(playlist, song);
        playlist.removeSong(song, target);
    }

    // 로그인한 사용자와 플레이리스트 소유자가 같은지 확인
    private void validatePlaylistOwner(Playlist playlist, User user) {
        if (!playlist.getUser().getUserId().equals(user.getUserId())) {
            throw new MavveException(ExceptionCode.AUTH_TOKEN_MISMATCH);
        }
    }

    // 플레이리스트 존재 여부 검사
    private Playlist getPlaylistOrThrow(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new MavveException(ExceptionCode.PLAYLIST_NOT_FOUND));
    }

}
