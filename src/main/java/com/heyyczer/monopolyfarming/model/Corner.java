package com.heyyczer.monopolyfarming.model;

import com.heyyczer.monopolyfarming.model.interfaces.ICorner;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Corner {

	@NonNull
	private ICorner corner;

}
