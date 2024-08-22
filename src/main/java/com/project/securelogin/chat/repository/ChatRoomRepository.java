package com.project.securelogin.chat.repository;

import com.project.securelogin.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 채팅방 이름으로 채팅방 찾기
    Optional<ChatRoom> findByChatRoomName(String chatRoomName);

    // 특정 사용자 ID로 참여 중인 채팅방 목록 조회
    List<ChatRoom> findByUsersId(Long userId);

    // 채팅방 이름으로 채팅방 목록 조회 (이름에 부분적으로 일치하는 채팅방 찾기)
    List<ChatRoom> findByChatRoomNameContaining(String partialName);

    // 최근에 생성된 채팅방 목록 조회 (예: 최신 10개)
    List<ChatRoom> findTop10ByOrderByCreatedAtDesc();
}
