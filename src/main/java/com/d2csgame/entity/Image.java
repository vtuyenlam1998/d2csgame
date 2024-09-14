package com.d2csgame.entity;

import com.d2csgame.entity.enumration.EActionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_image")
@Setter
@Getter
public class Image extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long actionId;
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private EActionType actionType;
    @Column(name = "is_primary")
    private boolean isPrimary;
    @Column(name = "file_path")
    private String filePath;
}
