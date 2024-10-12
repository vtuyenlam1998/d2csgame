package com.d2csgame.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class AccountProductId implements Serializable {
    private Long accountId;
    private Long productId;
}