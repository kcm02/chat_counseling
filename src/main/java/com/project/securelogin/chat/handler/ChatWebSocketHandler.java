package com.project.securelogin.chat.handler;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 채팅방과 연결된 세션을 저장하는 맵
    private final Map<Long, Set<WebSocketSession>> chatRooms = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 채팅방 ID를 쿼리 파라미터에서 가져옴
        String roomIdParam = (String) session.getAttributes().get("roomId");
        Long roomId = Long.parseLong(roomIdParam);

        chatRooms.computeIfAbsent(roomId, k -> new HashSet<>()).add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 모든 채팅방에서 세션 제거
        for (Set<WebSocketSession> sessions : chatRooms.values()) {
            sessions.remove(session);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지를 수신하면 채팅방 ID를 가져와서 해당 채팅방의 모든 세션에 브로드캐스트
        String payload = message.getPayload();
        String roomIdParam = (String) session.getAttributes().get("roomId");
        Long roomId = Long.parseLong(roomIdParam);

        Set<WebSocketSession> sessions = chatRooms.get(roomId);
        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                s.sendMessage(new TextMessage("Room " + roomId + ": " + payload));
            }
        }
    }
}
