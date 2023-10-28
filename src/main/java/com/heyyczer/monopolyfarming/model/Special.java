package com.heyyczer.monopolyfarming.model;

import java.util.List;

import javax.annotation.Nullable;

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
	private String name;

	@NonNull
	private List<String> description;

	@NonNull
	private Integer price;
	
	@NonNull
	private ISpecial special;

}
