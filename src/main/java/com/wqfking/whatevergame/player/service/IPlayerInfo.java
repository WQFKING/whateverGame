package com.wqfking.whatevergame.player.service;

import com.wqfking.whatevergame.player.dto.PlayerDTO;

public interface IPlayerInfo {
    String create(PlayerDTO dto) throws Exception;

}
