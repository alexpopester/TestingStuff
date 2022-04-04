package com.usu.rougelike.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;

import java.util.Random;

public class GameOverMessage extends GameObject {
    public GameOverMessage(Game game) {
        super(game);
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        // this object is a little different than the others as is doesn't have a position inherently.
        // its position is determined at render time.
        // here are some of the things that will be useful for you.
        // float height = game.getHeight(); // get the height of the game
        // float width = game.getWidth(); // get the width of the game
        boolean isPlaying = game.getGameState().get("playing");
        if (isPlaying) return;
        paint.setColor(Color.RED);
        paint.setTextSize(120);
        canvas.drawText("You Died", game.getWidth()/2, game.getHeight() / 2, paint);
    }
}
