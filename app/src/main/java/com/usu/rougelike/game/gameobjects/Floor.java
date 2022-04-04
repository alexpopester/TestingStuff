package com.usu.rougelike.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.usu.rougelike.R;
import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;
import com.usu.rougelike.game.gameengine.Location;

public class Floor extends GameObject {
    public Floor(Game game) {
        super(game);
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        // things you can do in this render method for reference.
        // Location coords = getState().get("coords"); // gets the objects row and column in the grid
        // int cellSize = game.getGameState().get("cellSize"); // gets the size of each cell from the game
        // int myX = (int)coords.x * cellSize; // gets the screen space x position
        // int myY = (int)coords.y * cellSize; // gets the screen space y position

        Location coords = getState().get("coords");
        int cellSize = game.getGameState().get("cellSize");
        int myX = (int)coords.x * cellSize;
        int myY = (int)coords.y * cellSize;

        canvas.translate(myX, myY);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#7f5539"));
        canvas.drawRect(0, 0, cellSize, cellSize, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0,0,cellSize,cellSize,paint);
        paint.setColor(Color.parseColor("#b08968"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(cellSize/8, cellSize/8, (cellSize/5)*3, (cellSize/4), paint);
        canvas.drawRect(cellSize/16 *6, cellSize/16 * 7 , (cellSize/16)*13, (cellSize/16)*9, paint);
        canvas.drawRect(cellSize/8, cellSize/8*6, (cellSize/5)*3, (cellSize/8)*7, paint);
    }
}
