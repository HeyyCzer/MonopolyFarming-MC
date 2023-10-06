package com.heyyczer.monopolyfarming.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@RequiredArgsConstructor
public class Property {

    @Nullable
    @Setter
    private GamePlayer owner;

    @NonNull
    private String name;
    @NonNull
    private Float price;

}
