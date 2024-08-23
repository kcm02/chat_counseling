package com.project.securelogin.global.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.securelogin.chat.dto.ChatMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatMessageJsonResponse {
    private final int statusCode;
    private final String message;
    private final ChatMessageDTO chatMessage;  // 단일 메시지 응답
    private final List<ChatMessageDTO> chatMessages;  // 메시지 리스트 응답

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
