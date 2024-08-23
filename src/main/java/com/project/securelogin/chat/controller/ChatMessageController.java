package com.project.securelogin.chat.controller;

import com.project.securelogin.chat.domain.ChatMessage;
import com.project.securelogin.chat.dto.ChatMessageDTO;
import com.project.securelogin.chat.service.ChatMessageService;
import com.project.securelogin.global.dto.ChatMessageJsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 메시지 생성
    @PostMapping
    public ResponseEntity<ChatMessageJsonResponse> createMessage(
            @RequestParam Long chatRoomId,
            @RequestParam Long senderId,
            @RequestParam String content,
            @RequestParam String messageType) {
        ChatMessage chatMessage = chatMessageService.createMessage(chatRoomId, senderId, content, messageType);
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.from(chatMessage);
        ChatMessageJsonResponse response = new ChatMessageJsonResponse(
                HttpStatus.CREATED.value(),
                "메시지가 성공적으로 생성되었습니다.",
                chatMessageDTO,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 메시지 업데이트
    @PutMapping("/{messageId}")
    public ResponseEntity<ChatMessageJsonResponse> updateMessage(
            @PathVariable Long messageId,
            @RequestParam String newContent,
            @RequestParam String newStatus) {
        ChatMessage updatedMessage = chatMessageService.updateMessage(messageId, newContent, newStatus);
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.from(updatedMessage);
        ChatMessageJsonResponse response = new ChatMessageJsonResponse(
                HttpStatus.OK.value(),
                "메시지가 성공적으로 업데이트되었습니다.",
                chatMessageDTO,
                null
        );
        return ResponseEntity.ok(response);
    }

    // 메시지 중요 표시/해제
    @PatchMapping("/{messageId}/importance")
    public ResponseEntity<ChatMessageJsonResponse> toggleMessageImportance(@PathVariable Long messageId) {
        ChatMessage updatedMessage = chatMessageService.toggleMessageImportance(messageId);
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.from(updatedMessage);
        ChatMessageJsonResponse response = new ChatMessageJsonResponse(
                HttpStatus.NO_CONTENT.value(),
                "메시지 중요도가 성공적으로 변경되었습니다.",
                null,
                null
        );
        return ResponseEntity.noContent().build();
    }

    // 메시지 삭제
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ChatMessageJsonResponse> deleteMessage(@PathVariable Long messageId) {
        chatMessageService.deleteMessage(messageId);
        ChatMessageJsonResponse response = new ChatMessageJsonResponse(
                HttpStatus.NO_CONTENT.value(),
                "메시지가 성공적으로 삭제되었습니다.",
                null,
                null
        );
        return ResponseEntity.noContent().build();
    }

    // 파일 URL 첨부
    @PatchMapping("/{messageId}/file")
    public ResponseEntity<ChatMessageJsonResponse> attachFileToMessage(
            @PathVariable Long messageId,
            @RequestParam String fileUrl) {
        ChatMessage updatedMessage = chatMessageService.attachFileToMessage(messageId, fileUrl);
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.from(updatedMessage);
        ChatMessageJsonResponse response = new ChatMessageJsonResponse(
                HttpStatus.NO_CONTENT.value(),
                "파일이 메시지에 성공적으로 첨부되었습니다.",
                null,
                null
        );
        return ResponseEntity.noContent().build();
    }

    // 답장 메시지 설정
    @PatchMapping("/{messageId}/reply")
    public ResponseEntity<ChatMessageJsonResponse> replyToMessage(
            @PathVariable Long messageId,
            @RequestParam Long replyToMessageId) {
        ChatMessage updatedMessage = chatMessageService.replyToMessage(messageId, replyToMessageId);
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.from(updatedMessage);
        ChatMessageJsonResponse response = new ChatMessageJsonResponse(
                HttpStatus.NO_CONTENT.value(),
                "답장 메시지가 성공적으로 설정되었습니다.",
                null,
                null
        );
        return ResponseEntity.noContent().build();
    }

    // 채팅방의 모든 메시지 조회
    @GetMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<ChatMessageJsonResponse> getMessagesByChatRoom(@PathVariable Long chatRoomId) {
        List<ChatMessage> chatMessages = chatMessageService.getMessagesByChatRoom(chatRoomId);
        List<ChatMessageDTO> chatMessageDTOs = chatMessages.stream()
                .map(ChatMessageDTO::from)
                .collect(Collectors.toList());
        ChatMessageJsonResponse response = new ChatMessageJsonResponse(
                HttpStatus.OK.value(),
                "채팅방의 모든 메시지가 성공적으로 조회되었습니다.",
                null,
                chatMessageDTOs
        );
        return ResponseEntity.ok(response);
    }
}
