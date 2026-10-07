package com.wqfking.whatevergame.player.controller;

import com.wqfking.whatevergame.player.dto.PlayerDTO;
import com.wqfking.whatevergame.player.entity.Player;
import com.wqfking.whatevergame.player.service.IPlayerInfo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {
    @Resource
    private IPlayerInfo iPlayerInfo;

    @PostMapping("/create")
    public ResponseEntity<String> createPlayer(@Valid @RequestBody PlayerDTO dto) throws Exception {
        return ResponseEntity.ok(iPlayerInfo.create(dto));

    }

}
