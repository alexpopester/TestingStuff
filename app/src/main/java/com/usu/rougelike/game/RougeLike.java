package com.usu.rougelike.game;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.usu.rougelike.game.gameengine.Game;
import com.usu.rougelike.game.gameengine.GameObject;
import com.usu.rougelike.game.gameengine.Input;
import com.usu.rougelike.game.gameengine.Layer;
import com.usu.rougelike.game.gameengine.Location;
import com.usu.rougelike.game.gameengine.State;
import com.usu.rougelike.game.gameobjects.Barrier;
import com.usu.rougelike.game.gameobjects.BossBarrier;
import com.usu.rougelike.game.gameobjects.BossFloor;
import com.usu.rougelike.game.gameobjects.BossMonster;
import com.usu.rougelike.game.gameobjects.Door;
import com.usu.rougelike.game.gameobjects.Floor;
import com.usu.rougelike.game.gameobjects.Fog;
import com.usu.rougelike.game.gameobjects.GameOverMessage;
import com.usu.rougelike.game.gameobjects.Hud;
import com.usu.rougelike.game.gameobjects.Key;
import com.usu.rougelike.game.gameobjects.Monster;
import com.usu.rougelike.game.gameobjects.Player;

import java.util.ArrayList;
import java.util.Random;

public class RougeLike extends Game {
    int numCols;
    int numRows;
    int cellSize;

    // this is an internal reference for all important objects
    GameObject player;
    GameObject key;
    GameObject door;
    GameObject hud;
    GameObject gameOverMessage;

    // these act as object pools so each object can be reused between levels
    ArrayList<GameObject> monsters;
    ArrayList<GameObject> barriers;
    ArrayList<GameObject> floorTiles;
    ArrayList<GameObject> fogTiles;
    ArrayList<GameObject> bossFloorTiles;
    ArrayList<GameObject> bossBarriers;

    public RougeLike(float width, float height) {
        super(width, height);
    }

    // init will be called automatically after instantiation is complete.
    @Override
    public void init() {
        numCols = 7;
        cellSize = (int)getWidth() / numCols;
        numRows = (int)(getHeight() - 50) / cellSize;
        System.out.println();
        // initialize layers
        // Layers describe the drawing order of game objects, in other words
        // objects in layer 1 will be drawn over top of objects in layer 0.
        // only object contained in the layers structure will be updated and rendered.
        ArrayList<Layer> layers = getLayers();
        // these 3 layers are static, meaning they will only be rendered and not updated
        // they don't need to be updated because they will never change once created.
        layers.add(new Layer()); // floor
        layers.get(0).isStatic = true;
        layers.add(new Layer()); // barriers
        layers.get(1).isStatic = true;
        layers.add(new Layer()); // key, door
        layers.get(2).isStatic = true;

        // these are dynamic layers, each object in these layers will
        // be updated in realtime
        layers.add(new Layer()); // monsters,
        layers.add(new Layer()); // player
        layers.add(new Layer()); // fog
        layers.add(new Layer()); // UI

        // initialize game state
        State gameState = getGameState();
        gameState.set("level", 1);
        gameState.set("playing", true); // will set to false when player dies
        gameState.set("cellSize", cellSize);
        gameState.set("numRows", numRows);
        gameState.set("numCols", numCols);
        gameState.set("turn",  "player");
        gameState.set("nextLevel", false);
        gameState.set("endTurn", false);
        gameState.set("bossLevel", 1);

        // init consistent objects
        player = new Player(this);
        this.tagObj("player", player);
        key = new Key(this);
        door = new Door(this);
        hud = new Hud(this);
        hud.getState().set("coords", new Location(0, getHeight()));
        gameOverMessage = new GameOverMessage(this);

        // Prepare object pools
        floorTiles = new ArrayList<>();
        monsters = new ArrayList<>();
        barriers = new ArrayList<>();
        fogTiles = new ArrayList<>();
        bossBarriers = new ArrayList<>();
        bossFloorTiles = new ArrayList<>();
        // init floor tiles
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                GameObject floor = new Floor(this);
                floor.getState().set("coords", new Location(j, i));
                floorTiles.add(floor);
            }
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                GameObject floor = new BossFloor(this);
                floor.getState().set("coords", new Location(j, i));
                bossFloorTiles.add(floor);
            }
        }

        // init fog tiles
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                GameObject fog = new Fog(this);
                fog.getState().set("coords", new Location(j, i));
                fogTiles.add(fog);
            }
        }

        // build the first level
        buildNewLevel();
    }

    // all games follow the same basic pattern
    // while(playing):
    //   update everything
    //   draw everything
    //
    // each step should work independendent of the other steps
    // doFrame is the update method
    @Override
    public void doFrame(long deltaTime) {
        // update all game objects over time
        getLayers().forEach((layer) -> {
            if (!layer.isStatic) {
                layer.gameObjects.forEach(go -> {
                    go.update(deltaTime);
                });
            }
        });

        // check to see if either the player or the monsters performed their turn.
        // player performs turn by tapping on a square
        // monsters automatically perform their turn based on their AI
        boolean endTurn = getGameState().get("endTurn");

        // if turn was ended then swap the turn state to whoever's turn it is next.
        if (endTurn) {
            String turn = getGameState().get("turn");
            if (turn == "player") {
                getGameState().set("turn", "monster");
            } else {
                getGameState().set("turn", "player");
            }
            getGameState().set("endTurn", false);
        }

        // check to see if the player completed the level
        boolean nextLevel = getGameState().get("nextLevel");

        // if player completed level then reset and rebuild new level.
        if (nextLevel) {
            key.getState().set("active", true);
            player.getState().set("hasKey", false);
            int currentLevel = getGameState().get("level");
            getGameState().set("level", currentLevel + 1);
            getGameState().set("turn", "player");

            // every 5 levels is a boss level
            // as the game goes on the boss gets harder and harder.
            if (currentLevel + 1 % 5 == 0) {
                getGameState().set("bossLevel", getGameState().<Integer>get("bossLevel") + 1);
            }
            buildNewLevel();
            getGameState().set("nextLevel", false);
        }
    }

    // the rest of the code in this file is dedicated to level generation.
    // the following steps are performed:
    // 1. Generate a primitive version of the level using char[][] (more details on that further down)
    // 2. Clear out all layers and pool / reset existing objects for reuse.
    // 3. Transform primitive level into complex level with game objects
    //      a. New objects are only created as needed (for example if there are more monsters in this level)
    // 4. Store the map in the gameState for game objects to reference at run time
    // 5. Copy objects into layers for updating and rendering.
    public void buildNewLevel() {
        char[][] newLevel = generateLevel();
        int currentLevelNumber = getGameState().get("level");
        boolean isBossLevel = currentLevelNumber % 5 == 0;
        // pool current layer objects
        getLayers().forEach(layer -> {
            layer.gameObjects.forEach(gameObject -> {
                if (gameObject instanceof Barrier) {
                    barriers.add(gameObject);
                }
                if (gameObject instanceof Floor) {
                    floorTiles.add(gameObject);
                }
                if (gameObject instanceof BossBarrier) {
                    bossBarriers.add(gameObject);
                }
                if (gameObject instanceof BossFloor) {
                    bossFloorTiles.add(gameObject);
                }
                if (gameObject instanceof Monster) {
                    gameObject.getState().set("alive", true);
                    monsters.add(gameObject);
                }
                if (gameObject instanceof Fog) {
                    gameObject.getState().set("status", "hidden");
                    fogTiles.add(gameObject);
                }
            });
            layer.gameObjects.clear();
        });

        GameObject[][] map = new GameObject[numRows][numCols];
        // create game objects
        for(int i = 0; i < newLevel.length; i ++) {
            for (int j = 0; j < newLevel[i].length; j++) {
                GameObject obj = null;
                if (newLevel[i][j] == '*') {
                    if (isBossLevel) {
                        obj = bossBarriers.isEmpty() ? new BossBarrier(this) : bossBarriers.remove(0);
                    } else {
                        obj = barriers.isEmpty() ? new Barrier(this) : barriers.remove(0);
                    }
                    getLayers().get(1).gameObjects.add(obj);
                }
                if (newLevel[i][j] == 'B') {
                    obj = monsters.isEmpty() ? new Monster(this) : monsters.remove(0);
                    getLayers().get(3).gameObjects.add(obj);
                }
                if (newLevel[i][j] == 'K' && !isBossLevel) {
                    obj = key;
                    getLayers().get(2).gameObjects.add(obj);
                }
                if (newLevel[i][j] == 'E') {
                    obj = door;
                    getLayers().get(2).gameObjects.add(obj);
                }
                if (newLevel[i][j] == 'P') {
                    obj = player;
                    getLayers().get(4).gameObjects.add(obj);
                }
                if (newLevel[i][j] == 'Z') {
                    obj = new BossMonster(this);
                    obj.getState().set("level", getGameState().get("bossLevel"));
                    getLayers().get(3).gameObjects.add(obj);
                }
                if (obj == null) continue;
                obj.getState().set("coords", new Location(j, i));
                map[i][j] = obj;
            }
        }

        getGameState().set("map", map);

        // copy objects into layers.
        if (isBossLevel) {
            getLayers().get(0).gameObjects.addAll(bossFloorTiles);
            bossFloorTiles.clear();
        } else {
            getLayers().get(0).gameObjects.addAll(floorTiles);
            floorTiles.clear();

        }
        getLayers().get(6).gameObjects.add(hud);
        getLayers().get(6).gameObjects.add(gameOverMessage);
        getLayers().get(5).gameObjects.addAll(fogTiles);
        fogTiles.clear();
    }


    // this method generates a primite version of the level
    // the following is how each object is represented
    // ' ' = floor
    // '*' = wall / barrier
    // 'P' = player
    // 'B' = monster
    // 'Z' = boss monster
    // 'K' = key
    // 'E' = exit

    public char[][] generateLevel() {
        char[][] level = new char[numRows][numCols];
        int currentLevelNumber =  getGameState().<Integer>get("level");
        addFloor(level);
        generateTerrain(level);
        addPlayer(level);
        addExit(level);
        addBandits(level, currentLevelNumber);
        if (currentLevelNumber % 5 == 0) {
            addExtra(level, 'Z');
        }
        addExtra(level, 'K');
        return level;
    }

    public static void addBandits(char[][] level, int currentLevel) {
        int count = (int)(Math.log(currentLevel) / Math.log(2));
        if (count == 0) {
            count = 1;
        }

        for (int i = 0; i < count; i++) {
            addExtra(level, 'B');
        }
    }

    public static void addExtra(char[][] level, char extra) {
        Random rng = new Random();
        int row = rng.nextInt(level.length - 2);
        int col = rng.nextInt(level[0].length - 2);

        boolean placed = false;
        while(!placed) {
            if (level[row][col] != ' ') {
                if (row == 0) {
                    col --;
                } else {
                    row --;
                }
            } else {
                level[row][col] = extra;
                placed = true;
            }
        }
    }
    // player is always placed at the bottom left corner
    public static void addPlayer(char[][] level) {
        level[level.length - 1][0] = 'P';
    }

    // exit is always at the top right corner
    public static void addExit(char[][] level) {
        level[0][level[0].length - 1] = 'E';
    }

    // fills the entire map with empty space. Spaces will later be replaced by other objs.
    public static void addFloor(char[][] level) {
        for(int row = 0; row < level.length; row++) {
            for (int col = 0; col < level[row].length; col ++) {
                level[row][col] = ' ';
            }
        }
    }

    // this is where the magic happens
    // 1. Randomly places barriers in the maps
    // 2. Does a process call cellular automata to generate unique terrain from
    //    the random placement. Its basically a few rounds of conways game of life!
    public static void generateTerrain(char[][] level) {
        randomlyPlaceWalls(level);
        char[][] generatedLevel = doCellularAutomata(level);
        // copy generateLevel into our actual level
        for(int row = 1; row < level.length - 1; row++) {
            for (int col = 1; col < level[row].length - 1; col ++) {
                level[row][col] = generatedLevel[row][col];
            }
        }
    }

    public static void randomlyPlaceWalls(char[][] level) {
        Random rng = new Random();
        for(int row = 1; row < level.length - 1; row++) {
            for (int col = 1; col < level[row].length - 1; col ++) {
                double random = rng.nextDouble();
                if (random > .6) {
                    level[row][col] = '*';
                }
            }
        }
    }

    public static char[][] doCellularAutomata(char[][] level) {
        char[][] tempLevel = copyLevel(level);

        for(int i = 0; i < 2; i++) {
            char[][] newTempLevel = new char[tempLevel.length][tempLevel[0].length];
            for (int row = 1; row < level.length - 1; row++) {
                for (int col = 1; col < level[row].length - 1; col++) {
                    int count = countNeighbors(tempLevel, row, col);
                    if (tempLevel[row][col] == '*' && count < 3) {
                        newTempLevel[row][col] = ' ';
                    } else if (tempLevel[row][col] == ' ' && count > 3) {
                        newTempLevel[row][col] = '*';
                    } else {
                        newTempLevel[row][col] = tempLevel[row][col];
                    }

                }
            }
            tempLevel = newTempLevel;
        }
        //
        return tempLevel;
    }

    public static int countNeighbors(char[][] level, int row, int col) {
        int count = 0;
        if (level[row - 1][col - 1] == '*') {
            count ++;
        }
        if (level[row - 1][col] == '*') {
            count ++;
        }
        if (level[row - 1][col + 1] == '*') {
            count ++;
        }
        if (level[row][col - 1] == '*') {
            count ++;
        }
        if (level[row][col + 1] == '*') {
            count ++;
        }
        if (level[row + 1][col - 1] == '*') {
            count ++;
        }
        if (level[row + 1][col] == '*') {
            count ++;
        }
        if (level[row + 1][col + 1] == '*') {
            count ++;
        }
        return count;
    }

    public static char[][] copyLevel(char[][] level) {
        char[][] levelCopy = new char[level.length][level[0].length];
        for(int row = 0; row < level.length; row++) {
            for (int col = 0; col < level[row].length; col ++) {
                levelCopy[row][col] = level[row][col];
            }
        }
        return levelCopy;
    }
}
