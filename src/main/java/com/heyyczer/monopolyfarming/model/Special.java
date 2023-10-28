package com.heyyczer.monopolyfarming.model;

import javax.annotation.Nullable;

import com.heyyczer.monopolyfarming.model.interfaces.ISpecial;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class Special {

    @Nullable
    @Setter
	private GamePlayer owner;
	
	@NonNull
	private ISpecial special;

}
