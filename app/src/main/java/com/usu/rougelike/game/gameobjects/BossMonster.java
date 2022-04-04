package com.usu.rougelike.game.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.OvalShape;

import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;
import com.usu.rougelike.game.gameengine.Location;

import java.util.ArrayList;
import java.util.Collections;

public class BossMonster extends GameObject {
    ArrayList<Integer> pattern = null;
    int turnNumber = 0;
    public BossMonster(Game game) {
        super(game);
        getState().set("alive", true);
        getState().set("health", 6);
        getState().set("level", 1); // will be overridden by game after instantiation
    }

    @Override
    public void update(long elapsedTime) {
        if (pattern == null) {
            pattern = new ArrayList<>();
            pattern.add(0);
            pattern.add(1);
            for (int i = 0; i < getState().<Integer>get("level"); i ++) {
                int move = (int)Math.round(Math.random());
                pattern.add(move);
            }
        }
        boolean isAlive = getState().get("alive");
        String turn = game.getGameState().get("turn");
        if (turn != "monster") return;
        game.getGameState().set("endTurn", true);
        if (!isAlive) return;
        int action = pattern.get(turnNumber % pattern.size());
        if (action == 1) { // move
            turnNumber ++;
            GameObject[][] map = game.getGameState().get("map");
            if (checkForPlayer()) {
                GameObject player = game.getGameObjectWithTag("player");
                Location playerLocation = player.getState().get("coords");
                Location myLocation = getState().get("coords");
                if (myLocation.x != playerLocation.x && myLocation.y != playerLocation.y) {
                    if (myLocation.y < playerLocation.y) {
                        GameObject other = map[(int)myLocation.y + 1][(int)myLocation.x];
                        if (other == null) {
                            map[(int)myLocation.y][(int)myLocation.x] = null;
                            myLocation.y = myLocation.y + 1;
                            map[(int)myLocation.y][(int)myLocation.x] = this;
                            return;
                        }
                    }
                    if (myLocation.y > playerLocation.y) {
                        GameObject other = map[(int)myLocation.y - 1][(int)myLocation.x];
                        if (other == null) {
                            map[(int)myLocation.y][(int)myLocation.x] = null;
                            myLocation.y = myLocation.y - 1;
                            map[(int)myLocation.y][(int)myLocation.x] = this;
                            return;
                        }
                    }
                    if (myLocation.x < playerLocation.x) {
                        GameObject other = map[(int)myLocation.y][(int)myLocation.x + 1];
                        if (other == null) {
                            map[(int)myLocation.y][(int)myLocation.x + 1] = null;
                            myLocation.x = myLocation.x + 1;
                            map[(int)myLocation.y][(int)myLocation.x] = this;
                            return;
                        }
                    }
                    if (myLocation.x > playerLocation.x) {
                        GameObject other = map[(int)myLocation.y][(int)myLocation.x - 1];
                        if (other == null) {
                            map[(int)myLocation.y][(int)myLocation.x] = null;
                            myLocation.x = myLocation.x - 1;
                            map[(int)myLocation.y][(int)myLocation.x] = this;
                            return;
                        }
                    }
                    moveRandom();

                } else if (myLocation.x == playerLocation.x) { // same column
                    if (myLocation.y < playerLocation.y) {
                        GameObject other = map[(int)myLocation.y + 1][(int)myLocation.x];
                        if (other instanceof Player) {
                            // end the game
                            other.getState().set("alive", false);
                            game.getGameState().set("playing", false);
                            return;
                        } else if (other == null) {
                            map[(int)myLocation.y][(int)myLocation.x] = null;
                            myLocation.y = myLocation.y + 1;
                            map[(int)myLocation.y][(int)myLocation.x] = this;
                            return;
                        }
                    }
                    if (myLocation.y > playerLocation.y) {
                        GameObject other = map[(int)myLocation.y - 1][(int)myLocation.x];
                        if (other instanceof Player) {
                            // end the game
                            other.getState().set("alive", false);
                            game.getGameState().set("playing", false);
                            return;
                        } else if (other == null) {
                            map[(int)myLocation.y][(int)myLocation.x] = null;
                            myLocation.y = myLocation.y - 1;
                            map[(int)myLocation.y][(int)myLocation.x] = this;
                            return;
                        }
                    }
                    moveRandom();
                } else if (myLocation.y == playerLocation.y) { // same row
                    if (myLocation.x < playerLocation.x) {
                        GameObject other = map[(int)myLocation.y][(int)myLocation.x + 1];
                        if (other instanceof Player) {
                            // end the game
                            other.getState().set("alive", false);
                            game.getGameState().set("playing", false);
                            return;
                        } else if (other == null) {
                            map[(int)myLocation.y][(int)myLocation.x] = null;
                            myLocation.x = myLocation.x + 1;
                            map[(int)myLocation.y][(int)myLocation.x] = this;
                            return;
                        }
                    }
                    if (myLocation.x > playerLocation.x) {
                        GameObject other = map[(int)myLocation.y][(int)myLocation.x - 1];
                        if (other instanceof Player) {
                            // end the game
                            other.getState().set("alive", false);
                            game.getGameState().set("playing", false);
                            return;
                        } else if (other == null) {
                            map[(int)myLocation.y][(int)myLocation.x] = null;
                            myLocation.x = myLocation.x - 1;
                            map[(int)myLocation.y][(int)myLocation.x] = this;
                            return;
                        }
                    }
                    moveRandom();
                }

            } else {
                moveRandom();
            }
        } else {
            turnNumber ++;
        }
    }

    private void moveRandom() {
        ArrayList<Integer> neighbors = new ArrayList();
        neighbors.add(1);
        neighbors.add(2);
        neighbors.add(3);
        neighbors.add(4);
        Collections.shuffle(neighbors);
        GameObject[][] map = game.getGameState().get("map");
        Location myLocation = getState().get("coords");
        while(!neighbors.isEmpty()) {
            int val = neighbors.remove(0);
            if(val == 1) {
                if (myLocation.y > 0 && map[(int)myLocation.y - 1][(int)myLocation.x] == null) {
                    map[(int)myLocation.y - 1][(int)myLocation.x] = this;
                    map[(int)myLocation.y][(int)myLocation.x] = null;
                    myLocation.y = myLocation.y - 1;
                    return;
                }
            }
            if (val == 2) {
                if (myLocation.x < map[0].length - 1 && map[(int)myLocation.y][(int)myLocation.x + 1] == null) {
                    map[(int)myLocation.y ][(int)myLocation.x + 1] = this;
                    map[(int)myLocation.y][(int)myLocation.x] = null;
                    myLocation.x = myLocation.x + 1;
                    return;
                }
            }
            if(val == 3) {
                if (myLocation.y < map.length - 1 && map[(int)myLocation.y + 1][(int)myLocation.x] == null) {
                    map[(int)myLocation.y + 1][(int)myLocation.x] = this;
                    map[(int)myLocation.y][(int)myLocation.x] = null;
                    myLocation.y = myLocation.y + 1;
                    return;
                }
            }
            if (val == 4) {
                if (myLocation.x > 0 && map[(int)myLocation.y][(int)myLocation.x - 1] == null) {
                    map[(int)myLocation.y ][(int)myLocation.x - 1] = this;
                    map[(int)myLocation.y][(int)myLocation.x] = null;
                    myLocation.x = myLocation.x - 1;
                    return;
                }
            }
        }
    }

    private boolean checkForPlayer() {
        GameObject player = game.getGameObjectWithTag("player");
        Location playerLocation = player.getState().get("coords");
        Location myLocation = getState().get("coords");
        double distance = Math.sqrt(Math.pow(playerLocation.x - myLocation.x, 2) + Math.pow(playerLocation.y - myLocation.y, 2));
        return distance < 5;
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        // things you can do in this render method for reference.
        // Location coords = getState().get("coords"); // gets the objects row and column in the grid
        // int cellSize = game.getGameState().get("cellSize"); // gets the size of each cell from the game
        // int myX = (int)coords.x * cellSize; // gets the screen space x position
        // int myY = (int)coords.y * cellSize; // gets the screen space y position
        // boolean isAlive = getState().get("alive"); // get whether or not the monster is alive
        // int level = getState().get("level"); // gets the difficulty level of the monster
        // int health = getState().get("health"); // gets how much health the monster has left

        Location coords = getState().get("coords");
        int cellSize = game.getGameState().get("cellSize");
        int myX = (int)coords.x * cellSize;
        int myY = (int)coords.y * cellSize;

        canvas.translate(myX, myY);
        boolean alive = getState().get("alive");
        if (alive) {
            paint.setColor(Color.MAGENTA);
        } else {
            paint.setColor(Color.CYAN);
        }
//        canvas.drawRect(0,0, cellSize, cellSize, paint);
        if (alive) {
            paint.setColor(Color.parseColor("#F51720"));
        }
        else {
            paint.setColor(Color.WHITE);
            canvas.drawRect(0,-100,180,0,paint);
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(20);
        drawTriangle(canvas,paint,0,0,100,0,0,-100);
        drawTriangle(canvas,paint,80,0,180,0,180,-100);
        canvas.drawRect(0,0, 180, 90, paint);
        drawTriangle(canvas,paint,0,90, 90, 180, 180, 90);
        paint.setColor(Color.BLACK);
        canvas.drawRect(60,90, 120, 120, paint);
        drawTriangle(canvas,paint, 60,120, 90, 160, 120, 120);
        canvas.drawCircle(60, 30, 10, paint);
        canvas.drawCircle(120, 30, 10, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeWidth(15);
        if (alive) {
            canvas.drawLine(80, 10, 20, -30, paint);
            canvas.drawLine(100, 10, 160, -30, paint);
            canvas.drawLine(40,70, 140, 70, paint);
        } else {
            canvas.drawLine(80, -60, 20, -30, paint);
            canvas.drawLine(80, -60, 20, -30, paint);
            canvas.drawLine(50,70, 130, 70, paint);
            canvas.drawLine(50, 50, 30,50, paint);
            canvas.drawLine(130, 50, 150,50, paint);
            canvas.drawLine(30, 30, 20,30, paint);
            canvas.drawLine(150, 30, 170,30, paint);
        }

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
