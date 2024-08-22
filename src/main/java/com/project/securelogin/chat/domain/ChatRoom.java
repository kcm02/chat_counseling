package com.project.securelogin.chat.domain;

import com.project.securelogin.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_room_name", nullable = false)
    private String chatRoomName; // 채팅방 이름

    @ManyToMany
    @JoinTable(
            name = "user_chat_room",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>(); // 채팅방에 참여하는 사용자

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 추가적인 메서드들...

    // 채팅방에 사용자 추가
    public void addUser(User user) {
        this.users.add(user);
        user.getChatRooms().add(this); // 양방향 연관관계 유지
    }

    // 채팅방에서 사용자 제거
    public void removeUser(User user) {
        this.users.remove(user);
        user.getChatRooms().remove(this); // 양방향 연관관계 유지
    }

    // 채팅방 이름 변경
    public void updateChatRoomName(String newName) {
        this.chatRoomName = newName;
    }

    // 채팅방에 참여한 사용자 목록 확인
    public Set<User> getUsers() {
        return this.users;
    }

    // 생성 시간 확인
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    // 업데이트 시간 확인
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }
}
