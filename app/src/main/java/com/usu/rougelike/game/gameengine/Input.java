package com.usu.rougelike.game.gameengine;

public class Input {
    public enum Type {
        Touch
    }
    public Type type;
    public Location location;

    public Input(Type type, Location location) {
        this.type = type;
        this.location = location;
    }
}
