package com.usu.rougelike.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;
import com.usu.rougelike.game.gameengine.Location;

public class Fog extends GameObject {
    public Fog(Game game) {
        super(game);
        getState().set("status", "hidden");
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        if (checkForPlayer()) {
            getState().set("status", "visible");
        } else {
            String status = getState().get("status");
            if (status == "visible") {
                getState().set("status", "visited");
            }
        }

    }

    private boolean checkForPlayer() {
        GameObject player = game.getGameObjectWithTag("player");
        Location playerLocation = player.getState().get("coords");
        Location myLocation = getState().get("coords");
        double distance = Math.sqrt(Math.pow(playerLocation.x - myLocation.x, 2) + Math.pow(playerLocation.y - myLocation.y, 2));
        return distance < 3;
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        // things you can do in this render method for reference.
        // Location coords = getState().get("coords"); // gets the objects row and column in the grid
        // int cellSize = game.getGameState().get("cellSize"); // gets the size of each cell from the game
        // int myX = (int)coords.x * cellSize; // gets the screen space x position
        // int myY = (int)coords.y * cellSize; // gets the screen space y position
        // String status = getState().get("status"); // gets the status of the fog, either "hidden", "visited", or "visible"'

//
//        String status = getState().get("status");
//        Location coords = getState().get("coords");
//        int cellSize = game.getGameState().get("cellSize");
//        int myX = (int)coords.x * cellSize;
//        int myY = (int)coords.y * cellSize;
//
//        canvas.translate(myX, myY);
//        if (status == "hidden") {
//            canvas.drawRect(0,0, cellSize, cellSize, paint);
//        } else if (status == "visited") {
//            paint.setColor(Color.argb(.1f, 0f,0f,0f));
//            canvas.drawRect(0,0, cellSize, cellSize, paint);
//        }
    }
}
