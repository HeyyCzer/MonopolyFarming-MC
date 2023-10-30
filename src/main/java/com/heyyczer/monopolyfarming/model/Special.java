package com.heyyczer.monopolyfarming.model;

import com.heyyczer.monopolyfarming.model.interfaces.ISpecial;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@RequiredArgsConstructor
public class Special {

    @Nullable
    @Setter
	private GamePlayer owner;
	
	@NonNull
	private ISpecial handler;

}
