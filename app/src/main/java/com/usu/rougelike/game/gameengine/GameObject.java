package com.usu.rougelike.game.gameengine;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameObject {
    protected Game game;
    protected State state = new State();
    public GameObject(Game game) {
        this.game = game;
    }

    public State getState() {
        return state;
    }

    public void update(long elapsedTime) {};

    public void render(Canvas canvas, Paint paint) {};


}
