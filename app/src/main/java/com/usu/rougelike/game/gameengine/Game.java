package com.usu.rougelike.game.gameengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class Game {
    private float width;
    private float height;
    private State gameState = new State();
    private HashMap<String, GameObject> taggedObjs = new HashMap<>();
    private ArrayList<Layer> layers = new ArrayList<>();


    protected ArrayList<Input> inputQueue = new ArrayList<>();

    public Game(float width, float height) {
        this.width = width;
        this.height = height;
        init();
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public final float getHeight() {
        return height;
    }

    public final float getWidth() {
        return width;
    }

    public final void enqueueInput(Input.Type type, float x, float y) {
        Input newInput = new Input(type, new Location(x, y));
        inputQueue.add(newInput);
    }

    public final State getGameState() {
        return gameState;
    }

    public ArrayList<Input> getInputQueue() {
        return inputQueue;
    }

    public final void clearInputQueue() {
        inputQueue.clear();
    }

    public final void tagObj(String tag, GameObject obj) {
        this.taggedObjs.put(tag, obj);
    }

    public final GameObject getGameObjectWithTag(String tag) {
        return this.taggedObjs.get(tag);
    }

    public void init() {}

    public abstract void doFrame(long deltaTime);

    public final void render(Canvas canvas, Paint paint) {
        layers.forEach(layer -> {
            layer.gameObjects.forEach(go -> {
                canvas.save();
                go.render(canvas, paint);
                paint.reset();
                canvas.restore();
            });
        });
    };

}
