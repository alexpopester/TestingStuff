package com.usu.rougelike.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;
import com.usu.rougelike.game.gameengine.Location;

public class Hud extends GameObject {
    public Hud(Game game) {
        super(game);
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        // things you can do in this render method for reference.
        // Location coords = getState().get("coords"); // gets the objects row and column in the grid
        // int cellSize = game.getGameState().get("cellSize"); // gets the size of each cell from the game
        // int myX = (int)coords.x * cellSize; // gets the screen space x position
        // int myY = (int)coords.y * cellSize; // gets the screen space y position
        // GameObject player = game.getGameObjectWithTag("player"); // get the player
        // boolean hasKey = player.getState().get("hasKey"); // get whether the player has the key
        // int level = game.getGameState().get("level"); // the current level number the player is on

        Location coords = getState().get("coords");
        int myX = (int)coords.x;
        int myY = (int)coords.y;

        canvas.translate(myX, myY);
        int level = game.getGameState().get("level");
        paint.setTextSize(60);
        GameObject player = game.getGameObjectWithTag("player");
        boolean hasKey = player.getState().get("hasKey");
        canvas.drawText("LEVEL: " + level + "  KEY: " + hasKey, 15, -15, paint);

    }
}
