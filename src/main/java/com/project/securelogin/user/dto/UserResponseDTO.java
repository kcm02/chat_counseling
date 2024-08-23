package com.project.securelogin.user.dto;

import com.project.securelogin.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {
    private final Long id;
    private final String username;
    private final String email;

    // User 엔티티를 UserResponseDTO로 변환하는 메서드
    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
