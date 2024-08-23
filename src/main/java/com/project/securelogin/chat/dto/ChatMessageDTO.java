package com.project.securelogin.chat.dto;

import com.project.securelogin.chat.domain.ChatMessage;
import com.project.securelogin.user.dto.UserResponseDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageDTO {
    private Long id;
    private ChatRoomDTO chatRoom; // 채팅방 정보 포함
    private UserResponseDTO sender; // 사용자 정보 포함
    private String content;
    private LocalDateTime timestamp;
    private String status;
    private String messageType;
    private LocalDateTime editedAt;
    private ChatMessageDTO replyToMessage; // 답장 메시지 정보
    private boolean isImportant;
    private String fileUrl;

    // ChatMessage 엔티티를 ChatMessageDTO로 변환하는 메서드
    public static ChatMessageDTO from(ChatMessage chatMessage) {
        return ChatMessageDTO.builder()
                .id(chatMessage.getId())
                .chatRoom(ChatRoomDTO.from(chatMessage.getChatRoom()))
                .sender(UserResponseDTO.from(chatMessage.getSender()))
                .content(chatMessage.getContent())
                .timestamp(chatMessage.getTimestamp())
                .status(chatMessage.getStatus())
                .messageType(chatMessage.getMessageType())
                .editedAt(chatMessage.getEditedAt())
                .replyToMessage(chatMessage.getReplyToMessage() != null
                        ? from(chatMessage.getReplyToMessage())
                        : null)
                .isImportant(chatMessage.isImportant())
                .fileUrl(chatMessage.getFileUrl())
                .build();
    }
}
