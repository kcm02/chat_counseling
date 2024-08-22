package com.project.securelogin.chat.service;

import com.project.securelogin.chat.domain.ChatRoom;
import com.project.securelogin.chat.repository.ChatRoomRepository;
import com.project.securelogin.user.domain.User;
import com.project.securelogin.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 채팅방 생성
    public ChatRoom createChatRoom(String chatRoomName) {
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(chatRoomName)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    // 채팅방 이름 업데이트
    public ChatRoom updateChatRoomName(Long chatRoomId, String newName) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        chatRoom.updateChatRoomName(newName);
        return chatRoomRepository.save(chatRoom);
    }

    // 채팅방에 사용자 추가
    public void addUserToChatRoom(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        chatRoom.addUser(user);
        userRepository.save(user); // 사용자 엔티티 업데이트
        chatRoomRepository.save(chatRoom); // 채팅방 엔티티 업데이트
    }

    // 채팅방에서 사용자 제거
    public void removeUserFromChatRoom(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        chatRoom.removeUser(user);
        userRepository.save(user); // 사용자 엔티티 업데이트
        chatRoomRepository.save(chatRoom); // 채팅방 엔티티 업데이트
    }

    // 채팅방 조회
    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
    }

    // 모든 채팅방 조회
    public Iterable<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }
}
