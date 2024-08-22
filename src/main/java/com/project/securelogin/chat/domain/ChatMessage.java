package com.project.securelogin.chat.domain;

import com.project.securelogin.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;  // 메시지가 속한 채팅방

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;  // 메시지를 보낸 사용자

    private String content;  // 메시지 내용

    @CreationTimestamp
    private LocalDateTime timestamp;  // 메시지 전송 시간

    private String status; // 메시지 상태 (예: SENT, DELIVERED, READ)

    private String messageType; // 메시지 타입 (예: TEXT, IMAGE, FILE)

    @UpdateTimestamp
    private LocalDateTime editedAt; // 메시지 수정 시간

    @ManyToOne
    @JoinColumn(name = "reply_to_message_id")
    private ChatMessage replyToMessage; // 답장 대상 메시지

    private boolean isImportant; // 중요 메시지 여부

    private String fileUrl; // 첨부 파일 URL

    // 메시지 업데이트
    public void updateContent(String newContent) {
        this.content = newContent;
        this.editedAt = LocalDateTime.now();
    }

    // 메시지 상태 업데이트
    public void updateStatus(String newStatus) {
        if (isValidStatus(newStatus)) {
            this.status = newStatus;
        }
    }

    // 중요 메시지 표시
    public void markAsImportant() {
        this.isImportant = true;
    }

    // 중요 메시지 표시 취소
    public void markAsNotImportant() {
        this.isImportant = false;
    }

    // 메시지에 첨부된 파일 URL 설정
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    // 답장 메시지 설정
    public void setReplyToMessage(ChatMessage replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    // 메시지 삭제 메서드 (물리적 X 논리적 O)
    public void deleteMessage() {
        this.content = "[삭제된 메시지]";
        this.status = "DELETED";
    }

    // 메시지 내용 유효성 검증
    public boolean isContentValid() {
        return content != null && !content.trim().isEmpty();
    }

    // 메시지 최근 여부 확인
    public boolean isMessageRecent(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        return timestamp != null && timestamp.isAfter(now.minusMinutes(minutes));
    }

    // 메시지 상태 유효성 검증
    public boolean isValidStatus(String status) {
        return status != null && (status.equals("SENT") || status.equals("DELIVERED") || status.equals("READ") || status.equals("DELETED"));
    }
}
