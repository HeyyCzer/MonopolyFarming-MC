package com.heyyczer.monopolyfarming.model;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class Property {

    @Nullable
    @Setter
    private GamePlayer owner;

}
