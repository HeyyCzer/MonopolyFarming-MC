package com.heyyczer.monopolyfarming.model;

public interface IService {

    String getName();
    String getDescription();

    void callback(GamePlayer player);

}
