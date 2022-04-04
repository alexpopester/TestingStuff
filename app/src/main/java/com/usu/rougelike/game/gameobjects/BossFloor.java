package com.usu.rougelike.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;
import com.usu.rougelike.game.gameengine.Location;

public class BossFloor extends GameObject {
    public BossFloor(Game game) {
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
        paint.setColor(Color.parseColor("#000000"));
        canvas.drawRect(0, 0, cellSize, cellSize, paint);
        paint.setColor(Color.parseColor("#FAD02C"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        canvas.drawLine(cellSize/2, cellSize/2, cellSize/2 * 3, cellSize/2 * 3, paint);
        canvas.drawLine(cellSize/2, cellSize/2, cellSize/2, 0, paint);
        canvas.drawLine(cellSize/2, 0, cellSize, -cellSize/2, paint);
        canvas.drawLine(cellSize, -cellSize/2, cellSize, 0, paint);
        paint.setColor(Color.parseColor("#90ADC6"));
        canvas.drawLine(30, cellSize*15/16, cellSize,cellSize*15/16, paint);
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(8);
        for (int i = 0; i< cellSize; i += cellSize*8/16){
            for (int j = 0; j < cellSize; j += cellSize*8/16){
                canvas.drawCircle(i, j, cellSize/16, paint);
            }
//            paint.setColor(Color.parseColor("#000000"));
        }

    }
}
