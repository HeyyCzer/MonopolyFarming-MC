package com.heyyczer.monopolyfarming.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GamePlayer {

    @NonNull
    private UUID gameUUID;

    @NonNull
    private Player player;

    @Setter
    private NPC playerNpc;

    @Getter @Setter
    private int balance;

    @Setter
    private double rentMultiplier = 1.0;

}
