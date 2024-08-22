package com.project.securelogin.chat.repository;

import com.project.securelogin.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 채팅방 ID로 메시지 목록 조회
    List<ChatMessage> findByChatRoomId(Long chatRoomId);

    // 사용자 ID로 보낸 메시지 목록 조회
    List<ChatMessage> findBySenderId(Long senderId);

    // 답장 대상 메시지 ID로 메시지 목록 조회
    List<ChatMessage> findByReplyToMessageId(Long replyToMessageId);

    // 특정 상태의 메시지 목록 조회
    List<ChatMessage> findByStatus(String status);

    // 채팅방 ID와 상태로 메시지 목록 조회
    List<ChatMessage> findByChatRoomIdAndStatus(Long chatRoomId, String status);

    // 메시지 타입과 상태로 메시지 목록 조회
    List<ChatMessage> findByMessageTypeAndStatus(String messageType, String status);

    // 특정 채팅방에서 최신 메시지 조회 (예: 최신 10개)
    List<ChatMessage> findTop10ByChatRoomIdOrderByTimestampDesc(Long chatRoomId);

    // 특정 채팅방과 특정 사용자 ID로 메시지 조회
    List<ChatMessage> findByChatRoomIdAndSenderId(Long chatRoomId, Long senderId);

    // 중요 메시지 조회 (예: 채팅방 ID와 중요도 기준으로 메시지 조회)
    List<ChatMessage> findByChatRoomIdAndIsImportant(Long chatRoomId, boolean isImportant);
}
