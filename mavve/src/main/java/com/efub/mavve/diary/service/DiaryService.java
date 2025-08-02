package com.efub.mavve.diary.service;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.diary.domain.Diary;
import com.efub.mavve.diary.domain.Emoji;
import com.efub.mavve.diary.dto.request.DiaryCreateRequest;
import com.efub.mavve.diary.dto.request.DiaryUpdateRequest;
import com.efub.mavve.diary.dto.response.DiaryListResponse;
import com.efub.mavve.diary.dto.response.DiaryResponse;
import com.efub.mavve.diary.repository.DiaryRepository;
import com.efub.mavve.diary.repository.EmojiRepository;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import com.efub.mavve.songs.domain.Song;
import com.efub.mavve.songs.domain.SongArtist;
import com.efub.mavve.songs.repository.SongArtistRepository;
import com.efub.mavve.songs.service.SongShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final SongArtistRepository songArtistRepository;
    private final EmojiRepository emojiRepository;
    private final SongShareService songShareService;


    // 한 줄 일기 생성
    @Transactional
    public DiaryResponse createDiary(@Valid DiaryCreateRequest request, User user){
        Emoji emoji = findByEmojiId(request.emojiId());
        Song song =  findOrSaveSong(request.spotifySongId(), user);
        List<String> artistNames = findSongArtistBySongId(song.getSongId());

        Diary diary = diaryRepository.save(request.toEntity(user, emoji, song));
        return DiaryResponse.from(diary, artistNames);
    }

    // 한 줄 일기 수정
    @Transactional
    public DiaryResponse updateDiary(Long diaryId, @Valid DiaryUpdateRequest request, User user) {
        Diary diary = findByDiaryId(diaryId);
        authorizeUser(user, diary);

        if (request.comment() != null){diary.changeComment(request.comment());}
        if (request.emojiId() != null){diary.changeEmojiId(findByEmojiId(request.emojiId()));}
        if (request.spotifySongId() != null){
            Song song = findOrSaveSong(request.spotifySongId(), user);
            diary.changeSongId(song);}

        List<String> artistNames = findSongArtistBySongId(diary.getSong().getSongId());
        return DiaryResponse.from(diary, artistNames);
    }

    // 한 줄 일기 삭제
    @Transactional
    public void deleteDiary(Long diaryId, User user){
        Diary diary = findByDiaryId(diaryId);
        authorizeUser(user, diary);
        diaryRepository.delete(diary);
    }

    // 사용자의 한 줄 일기 조회
    @Transactional(readOnly = true)
    public DiaryResponse getUserDiary(User user){
        Diary diary = diaryRepository.findByUser(user)
                .orElseThrow(()-> new MavveException(ExceptionCode.DIARY_NOT_FOUND));
        List<String> artistNames = findSongArtistBySongId(diary.getSong().getSongId());
        return DiaryResponse.from(diary, artistNames);

    }

    //한 줄 일기 전체 조회
    @Transactional(readOnly = true)
    public DiaryListResponse getListDiary(){
        List<Diary> diaryList = diaryRepository.findTop6ByOrderByCreatedAtDesc();

        List<DiaryResponse> responseList = diaryList.stream()
                .map(diary -> {
                    List<String> artistNames = findSongArtistBySongId(diary.getSong().getSongId());
                    return DiaryResponse.from(diary, artistNames);})
                .collect(Collectors.toList());
        return DiaryListResponse.from(responseList);
    }

    // db에 이미 있는 노래인지 확인
    @Transactional
    private Song findOrSaveSong(String spotifySongId, User user){
        return songShareService.existSongBySpotifySongId(spotifySongId)
                ? songShareService.getSongBySpotifySongId(spotifySongId)
                : songShareService.saveSongBySpotifySongId(spotifySongId, user);
    }

    // 하루가 지나면 모든 일기 삭제
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    @Async
    public void deleteOldDiaries() {
        diaryRepository.deleteAll();
    }

    // diaryId로 조회
    @Transactional(readOnly = true)
    private Diary findByDiaryId(Long diaryId){
        return diaryRepository.findByDiaryId(diaryId)
                .orElseThrow(() -> new MavveException(ExceptionCode.DIARY_NOT_FOUND));}

    // emojiId로 조회
    @Transactional(readOnly = true)
    private Emoji findByEmojiId(Long emojiId){
        return emojiRepository.findByEmojiId(emojiId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이모지입니다."));}

    // songId로 연결된 아티스트 조회
    @Transactional(readOnly = true)
    private List<String> findSongArtistBySongId(Long songId){
        List<SongArtist> songArtists = songArtistRepository.findBySong_SongId(songId);
        if (songArtists.isEmpty()) {
            throw new IllegalArgumentException("SongId에 연결되는 가수들을 찾을 수 없습니다.");
        }
        return songArtists.stream()
                .map(songArtist -> songArtist.getArtist().getArtistName())
                .collect(Collectors.toList());
    }

    // 수정, 삭제 권한 확인
    private void authorizeUser(User user, Diary diary){
        if(!user.getUserId().equals(diary.getUser().getUserId())){
            throw new MavveException(ExceptionCode.ROOM_OWNER_MISMATCH);
        }
    }


}
