package com.project.securelogin.chat.dto;

import com.project.securelogin.chat.domain.ChatRoom;
import com.project.securelogin.user.dto.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ChatRoomDTO {
    private Long id;
    private String chatRoomName;
    private Set<UserResponseDTO> users; // 사용자 정보를 포함
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ChatRoom 엔티티를 ChatRoomDTO로 변환하는 메서드
    public static ChatRoomDTO from(ChatRoom chatRoom) {
        return new ChatRoomDTO(
                chatRoom.getId(),
                chatRoom.getChatRoomName(),
                chatRoom.getUsers().stream()
                        .map(UserResponseDTO::from)
                        .collect(Collectors.toSet()),
                chatRoom.getCreatedAt(),
                chatRoom.getUpdatedAt()
        );
    }
}
