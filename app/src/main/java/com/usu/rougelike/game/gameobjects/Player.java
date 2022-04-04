package com.usu.rougelike.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;
import com.usu.rougelike.game.gameengine.Input;
import com.usu.rougelike.game.gameengine.Location;

public class Player extends GameObject {
    public Player(Game game) {
        super(game);
        getState().set("hasKey", false);
        getState().set("alive", true);
    }

    @Override
    public void update(long elapsedTime) {
        boolean isAlive = getState().get("alive");
        if (!isAlive) return;
        if (game.getGameState().<String>get("turn") == "player" && !game.getInputQueue().isEmpty()) {
            Input input = game.getInputQueue().remove(0);
            game.clearInputQueue();
            int cellSize = game.getGameState().get("cellSize");
            int numRows = game.getGameState().get("numRows");
            int maxY = cellSize * numRows;
            if (input.location.y >= maxY) return;
            int row = (int)input.location.y / cellSize;
            int col = (int)input.location.x / cellSize;
            Location myLocation = getState().get("coords");
            // check if neighbor
            if (
                    (myLocation.x == col && (myLocation.y == row + 1 || myLocation.y == row - 1)) ||
                    (myLocation.y == row && (myLocation.x == col + 1 || myLocation.x == col - 1))
                )
            {
                //check what we tapped on
                GameObject[][] map = game.getGameState().get("map");
                if (map[row][col] instanceof Barrier) return; // don't move to barriers
                game.getGameState().set("endTurn", true);
                if (map[row][col] == null) {
                    map[row][col] = this;
                    map[(int)myLocation.y][(int)myLocation.x] = null;
                    myLocation.x = col;
                    myLocation.y = row;
                }
                if (map[row][col] instanceof Key) {
                    map[row][col].getState().set("active", false);
                    getState().set("hasKey", true);
                    map[row][col] = null;
                }
                if (map[row][col] instanceof Monster) {
                    map[row][col].getState().set("alive", false);
                    map[row][col] = null;
                }
                if (map[row][col] instanceof BossMonster) {
                    int health = map[row][col].getState().get("health");
                    getState().set("hasKey", true);
                    if (health == 1) { //
                        map[row][col].getState().set("alive", false);
                    } else {
                        map[row][col].getState().set("health", health - 1);
                    }
                }
                boolean hasKey = getState().get("hasKey");
                if (map[row][col] instanceof Door && hasKey) {
                    game.getGameState().set("nextLevel", true);
                }
            }
        }
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        // things you can do in this render method for reference.
        // Location coords = getState().get("coords"); // gets the objects row and column in the grid
        // int cellSize = game.getGameState().get("cellSize"); // gets the size of each cell from the game
        // int myX = (int)coords.x * cellSize; // gets the screen space x position
        // int myY = (int)coords.y * cellSize; // gets the screen space y position
        // boolean hasKey = getState().get("hasKey"); // get whether the player has the key or not
        // boolean isAlive = getState().get("alive"); // get whether or not the player is alive


        Location coords = getState().get("coords");
        int cellSize = game.getGameState().get("cellSize");
        int myX = (int)coords.x * cellSize;
        int myY = (int)coords.y * cellSize;

        // TODO: Draw something interesting that represents the player
        canvas.translate(myX, myY);
        paint.setColor(Color.parseColor("#D2B48C"));
        canvas.drawRect(cellSize*5/16, 0, cellSize*11/16, cellSize*5/16, paint);
        paint.setColor(Color.BLACK);
        canvas.drawRect(cellSize*6/16, cellSize/16, cellSize*7/16, cellSize*3/16, paint);
        canvas.drawRect(cellSize*8/16, cellSize/16, cellSize*9/16, cellSize*3/16, paint);
        paint.setColor(Color.GREEN);
        canvas.drawRect(cellSize*3/16, cellSize*5/16, cellSize*13/16, cellSize*9/16, paint);
        paint.setColor(Color.BLUE);
        canvas.drawRect(cellSize*5/16, cellSize*9/16, cellSize*7/16, cellSize*14/16, paint);
        canvas.drawRect(cellSize*9/16, cellSize*9/16, cellSize*11/16, cellSize*14/16, paint);
    }
    public void drawTriangle(Canvas canvas, Paint paint, int x1,int x2, int y1, int y2, int z1, int z2) {

        Path path = new Path();
        path.moveTo(x1, x2); // Top
        path.lineTo(y1, y2); // Bottom left
        path.lineTo(z1, z2); // Bottom right
        path.lineTo(x1, x2); // Back to Top
        path.close();

        canvas.drawPath(path, paint);
    }
}
