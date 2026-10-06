package com.wqfking.whatevergame.player.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class Player {

    private final String playerId;

    private String nickname;

    private final Instant createdAt;

    //playerId and createdAt(Time) must be provided when creating new player.
    public Player(String playerId, String nickname) {
        this.playerId = Objects.requireNonNull(playerId);
        this.nickname = validateNickname(nickname);
        this.createdAt = Instant.now();
    }

    public void changeNickname(String nickname) {
        this.nickname = validateNickname(nickname);
    }

    private static String validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("昵称不能为空");
        }
        return nickname.trim();
    }
}

