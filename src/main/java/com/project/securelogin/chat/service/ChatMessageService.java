package com.project.securelogin.chat.service;

import com.project.securelogin.chat.domain.ChatMessage;
import com.project.securelogin.chat.domain.ChatRoom;
import com.project.securelogin.user.domain.User;
import com.project.securelogin.chat.repository.ChatMessageRepository;
import com.project.securelogin.chat.repository.ChatRoomRepository;
import com.project.securelogin.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 메시지 생성
    public ChatMessage createMessage(Long chatRoomId, Long senderId, String content, String messageType) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용이 비어있을 수 없습니다.");
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .messageType(messageType)
                .status("SENT")
                .isImportant(false) // 중요도는 기본적으로 false로 설정
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    // 메시지 업데이트
    public ChatMessage updateMessage(Long messageId, String newContent, String newStatus) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));

        if (!chatMessage.isContentValid()) {
            throw new IllegalArgumentException("메시지 내용이 유효하지 않습니다.");
        }

        chatMessage.updateContent(newContent);

        if (chatMessage.isValidStatus(newStatus)) {
            chatMessage.updateStatus(newStatus);
        } else {
            throw new IllegalArgumentException("유효하지 않은 메시지 상태입니다.");
        }

        return chatMessageRepository.save(chatMessage);
    }

    // 메시지 중요 표시/해제
    public ChatMessage toggleMessageImportance(Long messageId) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));

        // 현재 중요 여부를 반전
        if (chatMessage.isImportant()) {
            chatMessage.markAsNotImportant(); // 중요 표시 제거
        } else {
            chatMessage.markAsImportant(); // 중요 표시
        }

        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    // 메시지 삭제
    public void deleteMessage(Long messageId) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));

        chatMessage.deleteMessage();
        chatMessageRepository.save(chatMessage);
    }

    // 파일 URL 첨부
    public ChatMessage attachFileToMessage(Long messageId, String fileUrl) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));

        chatMessage.setFileUrl(fileUrl);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    // 답장 메시지 설정
    public ChatMessage replyToMessage(Long messageId, Long replyToMessageId) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));
        ChatMessage replyToMessage = chatMessageRepository.findById(replyToMessageId)
                .orElseThrow(() -> new IllegalArgumentException("답장 대상 메시지가 존재하지 않습니다."));

        chatMessage.setReplyToMessage(replyToMessage);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    // 채팅방의 모든 메시지 조회
    public List<ChatMessage> getMessagesByChatRoom(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }
}
