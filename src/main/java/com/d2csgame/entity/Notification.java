package com.d2csgame.entity;

import com.d2csgame.entity.enumration.EActionNotification;
import com.d2csgame.entity.enumration.ENotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "tbl_notification")
public class Notification extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String recipient;
    private LocalDateTime createdAt;
    @Column(name = "href")
    private String href;
    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private ENotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private EActionNotification action;

    @Column(name = "isRead")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
