package com.wqfking.whatevergame.player.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PlayerDTO {

//    private String playerId;

    @NotBlank
    private String nickname;
//
//    private Instant createdAt;

}