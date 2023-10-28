package com.heyyczer.monopolyfarming.model;

import java.util.UUID;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.citizensnpcs.api.npc.NPC;

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
	
	@Getter
	@Setter
	private int position;

    @Setter
    private double rentMultiplier = 1.0;

}
