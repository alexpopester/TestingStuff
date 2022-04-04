package com.usu.rougelike.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;
import com.usu.rougelike.game.gameengine.Location;

public class Door extends GameObject {
    public Door(Game game) {
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
        paint.setColor(Color.parseColor("#4C5270"));
        canvas.drawRect(10, 10, cellSize - 10, cellSize - 10, paint);
        paint.setColor(Color.parseColor("#C46200"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        canvas.drawRect(15, 15, cellSize - 15, cellSize - 15, paint);
        paint.setColor(Color.BLACK);
        canvas.drawLine(cellSize/3, 10, cellSize/3, cellSize-15, paint);
        canvas.drawLine(cellSize*2/3, 10, cellSize*2/3, cellSize-15, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(cellSize*12/16,cellSize*8/16, cellSize*2/16, paint);

    }
}
