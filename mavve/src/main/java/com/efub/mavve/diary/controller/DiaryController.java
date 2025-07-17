package com.efub.mavve.diary.controller;
import com.efub.mavve.auth.domain.User;
import com.efub.mavve.diary.dto.request.DiaryCreateRequest;
import com.efub.mavve.diary.dto.request.DiaryUpdateRequest;
import com.efub.mavve.diary.dto.response.DiaryListResponse;
import com.efub.mavve.diary.dto.response.DiaryResponse;
import com.efub.mavve.diary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
public class DiaryController {
    private final DiaryService diaryService;

    // 한 줄 일기 생성
    @PostMapping
    public ResponseEntity<DiaryResponse> createDiary(@AuthenticationPrincipal User user,
                                                     @Valid @RequestBody DiaryCreateRequest request){
        DiaryResponse response = diaryService.createDiary(request, user);
        Long diaryId = response.getDiaryId();
        return ResponseEntity.created(URI.create("/diaries/"+diaryId)).body(response);
    }

    //한 줄 일기 수정
    @PatchMapping("/{diaryId}")
    public ResponseEntity<DiaryResponse> updateDiary(@PathVariable("diaryId") Long diaryId,
                                                     @AuthenticationPrincipal User user,
                                                     @Valid @RequestBody DiaryUpdateRequest request){
        DiaryResponse response = diaryService.updateDiary(diaryId, request, user);
        return ResponseEntity.ok(response);
    }

    //한 줄 일기 삭제
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable("diaryId") Long diaryId,
                                            @AuthenticationPrincipal User user){
        diaryService.deleteDiary(diaryId, user);
        return ResponseEntity.noContent().build();
    }

    //사용자의 한 줄 일기 단일 조회
    @GetMapping("/user")
    public ResponseEntity<DiaryResponse> getUserDiary(@AuthenticationPrincipal User user){
        DiaryResponse response = diaryService.getUserDiary(user);
        return ResponseEntity.ok(response);
    }

    //한 줄 일기 전체 조회
    @GetMapping
    public ResponseEntity<DiaryListResponse> getListDiary(){
        DiaryListResponse response = diaryService.getListDiary();
        return ResponseEntity.ok(response);
    }
}
