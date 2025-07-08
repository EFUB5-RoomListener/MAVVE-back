package com.efub.mavve.global.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    @Value("${youtube.api.key}")
    private String apiKey;

    private final YouTube youtube;

    //노래 제목으로 youtube id 검색하기
    public List<String> findYoutubeIdBySongName(String artist, String name) throws IOException {
        YouTube.Search.List search = youtube.search().list(Collections.singletonList("id,snippet"));
        search.setKey(apiKey);
        search.setQ(artist + " " + name);
        //search.setVideoCategoryId("10");    //NOTE: 음악 카테고리 추가하면 MV가 검색결과에서 사라짐
        search.setType(Collections.singletonList("video"));
        search.setMaxResults(5L);
        // 검색 결과
        List<SearchResult> searchResultList = search.execute().getItems();

        List<String> result = new ArrayList<>();
        for(SearchResult s : searchResultList){
            String videoId = s.getId().getVideoId();
            String videoTitle = s.getSnippet().getTitle();
            result.add("Title: " + videoTitle + "\nURL: https://www.youtube.com/watch?v=" + videoId);
        }
        return result;
    }
}
