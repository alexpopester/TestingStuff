package com.usu.rougelike.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;
import com.usu.rougelike.game.gameengine.Location;

public class Key extends GameObject {
    public Key(Game game) {
        super(game);
        getState().set("active", true);
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        // things you can do in this render method for reference.
        // Location coords = getState().get("coords"); // gets the objects row and column in the grid
        // int cellSize = game.getGameState().get("cellSize"); // gets the size of each cell from the game
        // int myX = (int)coords.x * cellSize; // gets the screen space x position
        // int myY = (int)coords.y * cellSize; // gets the screen space y position
        // boolean isActive = getState().get("active"); // get whether the key is active or not (not active means the player already picked it up)

        boolean isActive = getState().get("active");
        if (!isActive) return;

        Location coords = getState().get("coords");
        int cellSize = game.getGameState().get("cellSize");
        int myX = (int)coords.x * cellSize;
        int myY = (int)coords.y * cellSize;

        canvas.translate(myX, myY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(cellSize*2 / 8, cellSize*2 / 8, cellSize / 6, paint);
        paint.setStrokeWidth(12);
        canvas.drawLine(cellSize*8/24, cellSize*8/24, cellSize*14/16, cellSize*14/16, paint);
        canvas.drawLine(cellSize*9/16,cellSize*9/16, cellSize*11/16,cellSize*7/16,paint);
        canvas.drawLine(cellSize*11/16,cellSize*11/16, cellSize*13/16,cellSize*9/16,paint);
        canvas.drawLine(cellSize*13/16,cellSize*13/16, cellSize*15/16,cellSize*11/16,paint);
    }
}
