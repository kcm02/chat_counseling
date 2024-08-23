package com.project.securelogin.chat.controller;

import com.project.securelogin.chat.domain.ChatRoom;
import com.project.securelogin.chat.dto.ChatRoomDTO;
import com.project.securelogin.chat.service.ChatRoomService;
import com.project.securelogin.global.dto.ChatRoomJsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ChatRoomJsonResponse> createChatRoom(@RequestParam String chatRoomName) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(chatRoomName);
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.from(chatRoom);
        ChatRoomJsonResponse response = new ChatRoomJsonResponse(HttpStatus.CREATED.value(), "채팅방이 성공적으로 생성되었습니다.", List.of(chatRoomDTO));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 채팅방 이름 업데이트
    @PutMapping("/{chatRoomId}/name")
    public ResponseEntity<ChatRoomJsonResponse> updateChatRoomName(@PathVariable Long chatRoomId, @RequestParam String newName) {
        ChatRoom updatedChatRoom = chatRoomService.updateChatRoomName(chatRoomId, newName);
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.from(updatedChatRoom);
        ChatRoomJsonResponse response = new ChatRoomJsonResponse(HttpStatus.OK.value(), "채팅방 이름이 성공적으로 업데이트되었습니다.", List.of(chatRoomDTO));
        return ResponseEntity.ok(response);
    }

    // 채팅방에 사용자 추가
    @PostMapping("/{chatRoomId}/users/{userId}")
    public ResponseEntity<ChatRoomJsonResponse> addUserToChatRoom(@PathVariable Long chatRoomId, @PathVariable Long userId) {
        chatRoomService.addUserToChatRoom(chatRoomId, userId);
        ChatRoomJsonResponse response = new ChatRoomJsonResponse(HttpStatus.NO_CONTENT.value(), "사용자가 채팅방에 성공적으로 추가되었습니다.", null);
        return ResponseEntity.noContent().build();
    }

    // 채팅방에서 사용자 제거
    @DeleteMapping("/{chatRoomId}/users/{userId}")
    public ResponseEntity<ChatRoomJsonResponse> removeUserFromChatRoom(@PathVariable Long chatRoomId, @PathVariable Long userId) {
        chatRoomService.removeUserFromChatRoom(chatRoomId, userId);
        ChatRoomJsonResponse response = new ChatRoomJsonResponse(HttpStatus.NO_CONTENT.value(), "사용자가 채팅방에서 성공적으로 제거되었습니다.", null);
        return ResponseEntity.noContent().build();
    }

    // 채팅방 조회
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomJsonResponse> getChatRoom(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.from(chatRoom);
        ChatRoomJsonResponse response = new ChatRoomJsonResponse(HttpStatus.OK.value(), "채팅방이 성공적으로 조회되었습니다.", List.of(chatRoomDTO));
        return ResponseEntity.ok(response);
    }

    // 모든 채팅방 조회
    @GetMapping
    public ResponseEntity<ChatRoomJsonResponse> getAllChatRooms() {
        List<ChatRoom> chatRooms = (List<ChatRoom>) chatRoomService.getAllChatRooms();
        List<ChatRoomDTO> chatRoomDTOs = chatRooms.stream()
                .map(ChatRoomDTO::from)
                .collect(Collectors.toList());
        ChatRoomJsonResponse response = new ChatRoomJsonResponse(HttpStatus.OK.value(), "모든 채팅방이 성공적으로 조회되었습니다.", chatRoomDTOs);
        return ResponseEntity.ok(response);
    }
}
