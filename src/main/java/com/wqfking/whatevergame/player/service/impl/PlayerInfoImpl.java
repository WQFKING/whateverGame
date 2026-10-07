package com.wqfking.whatevergame.player.service.impl;

import com.wqfking.whatevergame.player.dto.PlayerDTO;
import com.wqfking.whatevergame.player.entity.Player;
import com.wqfking.whatevergame.player.service.IPlayerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerInfoImpl implements IPlayerInfo {

    private static final Map<String,Player> TEMPSTORE = new ConcurrentHashMap<>();
    private static final Logger logger = LogManager.getLogger(PlayerInfoImpl.class);
    @Override
    public String create(PlayerDTO dto) throws Exception {
        UUID uuid = UUID.randomUUID();
        save(dto,uuid);
        return uuid.toString();
    }

    private boolean save(PlayerDTO dto,UUID uuid) throws Exception{
        if (dto.getNickname() == null){
            throw new Exception("nickname invalid !");
        }
        Player player = new Player(uuid.toString(),dto.getNickname());
        TEMPSTORE.put(uuid.toString(),player);
        logger.info(TEMPSTORE.toString());
        return true;
    }
}
