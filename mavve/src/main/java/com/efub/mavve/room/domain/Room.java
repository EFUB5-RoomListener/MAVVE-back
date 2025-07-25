package com.efub.mavve.room.domain;

import com.efub.mavve.auth.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "rooms")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private String roomName;

    @Column(nullable = false)
    private Long viewCount;

    @Column
    private List<String> tag;

    @Column(nullable = false)
    private boolean isPublic;

    @Column
    private String imageURL;

    @CreatedDate
    private LocalDateTime createdAt;


    // 빌더
    @Builder
    public Room(User user, String roomName, List<String> tag, boolean isPublic, String imageURL) {
        this.user = user;
        this.roomName = roomName;
        this.tag = tag;
        this.viewCount = 0L;
        this.imageURL = imageURL;
        this.isPublic = isPublic;
    }

    // 방 제목 수정
    public void changeRoomName(String roomName){
        this.roomName = roomName;
    }

    // 태그 수정
    public void changeTag(List<String> tag){
        this.tag = tag;
    }

    // 공개 여부 수정
    public void changeIsPublic(boolean isPublic){
        this.isPublic = isPublic;
    }

    // 방 이미지 변경
    public void changeImageURL(String imageURL) {this.imageURL = imageURL;}
}
