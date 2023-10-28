package com.heyyczer.monopolyfarming.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Corner {

	@NonNull
	private String name;

	@NonNull
	private List<String> description;

	@NonNull
	private ICorner corner;

}
