package com.usu.rougelike.game.gameengine;

import java.util.HashMap;

public class State {
    private HashMap<String, Object> state = new HashMap<>();

    public <T> T get(String key) {
       return (T) state.get(key);
    }

    public void set(String key, Object o) {
        state.put(key, o);
    }
}
