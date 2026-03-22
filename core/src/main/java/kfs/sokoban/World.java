package kfs.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import kfs.sokoban.comp.*;
import kfs.sokoban.ecs.Entity;
import kfs.sokoban.ecs.KfsSystem;
import kfs.sokoban.ecs.KfsWorld;
import kfs.sokoban.sys.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;


public class World extends KfsWorld {
    public static final int TILE_SIZE = 32;

    private final Consumer<Boolean> gameOverCallback;
    private final Consumer<String> setInfo;
    private final Map<Tile, Texture> textures;
    private final Sprite runa;
    private final Texture texRuna;

    private int height;
    private int width;

    public World(Consumer<Boolean> gameOverCallback, Consumer<String> setInfo, Camera camera, String mapFile) {
        this.gameOverCallback = gameOverCallback;
        this.setInfo = setInfo;
        this.textures = new EnumMap<>(Tile.class);

        super.addSys(new CameraFollowSystem(this, camera)); // set player after create
        super.addSys(new MovementSystem(this));
        super.addSys(new MovementAnimationSystem(this));
        super.addSys(new RenderSystem(this));

        texRuna = new Texture(Gdx.files.internal("textures/runa.png"));
        runa = new Sprite(texRuna);

        textures.put(Tile.PLAYER_D, new Texture("textures/robot_front.png"));
        textures.put(Tile.PLAYER_R, new Texture("textures/robot_right.png"));
        textures.put(Tile.PLAYER_L, new Texture("textures/robot_left.png"));
        textures.put(Tile.PLAYER_U, new Texture("textures/robot_back.png"));
        textures.put(Tile.WALL, new Texture("textures/wall.png"));
        textures.put(Tile.BOX, new Texture("textures/box.png"));
        textures.put(Tile.GOAL, new Texture("textures/goal.png"));
        textures.put(Tile.FLOOR, new Texture("textures/floor.png"));

        loadMap(mapFile);

        super.addSys(new LevelScoreSystem(this));
        super.addSys(new RuneEffectSystem(this));
        super.addSys(new FailureEffectSystem(this, camera));
    }

    private void loadMap(String file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            Gdx.files.internal(file).read()))) {
            String[] lines = reader.lines().toArray(String[]::new);
            height = lines.length+2;
            width = lines[0].length()+2;

            // make wall around map
            for (int h = 0; h < height; h++) {
                createWall(0, h);
                createWall(width-1, h);
            }
            for (int w = 1; w < width-1; w++) {
                createWall(w, 0);
                createWall(w, height -1);
            }

            for (int lineY = 0; lineY < lines.length; lineY++) {
                String line = lines[lines.length-lineY-1]; // převrátíme Y
                for (int lineX = 0; lineX < lines[0].length(); lineX++) {
                    char c = line.charAt(lineX);
                    Tile tile = Tile.fromCode(c);
                    if (tile == null) {
                        Gdx.app.error("World", "Unknown tile: char: "+c+" [" + lineX + ", " + lineY +"] file: "+file);
                    }
                    if (tile != Tile.FLOOR) {
                        Entity entity = createEntity();
                        PositionComponent pc = new PositionComponent(lineX+1, lineY+1);
                        addComponent(entity, pc);
                        addComponent(entity, new RenderComponent(tile));
                        if (tile == Tile.WALL) {
                            addComponent(entity, new WallComponent());
                        } else if (tile == Tile.PLAYER_L || tile == Tile.PLAYER_U || tile == Tile.PLAYER_R || tile == Tile.PLAYER_D) {
                            addComponent(entity, new PlayerComponent());
                            addComponent(entity, new InputComponent());
                            getSystem(CameraFollowSystem.class).cameraEntityPosition = pc;
                        } else if (tile == Tile.GOAL) {
                            addComponent(entity, new GoalComponent());
                        } else if (tile == Tile.BOX) {
                            addComponent(entity, new BoxComponent());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Gdx.app.error("World", "Failed to load map: " + e.getMessage());
        }
    }

    private void createWall(int x, int y) {
        Entity entity = createEntity();
        addComponent(entity, new PositionComponent(x, y));
        addComponent(entity, new RenderComponent(Tile.WALL));
        addComponent(entity, new WallComponent());

    }

    public void dispose() {
        textures.values().forEach(Texture::dispose);
        runSystems(KfsSystem::done);
        texRuna.dispose();
    }

    public Texture getTexture(Tile tile) {
        return textures.get(tile);
    }

    public void gameOver(boolean win) {
        gameOverCallback.accept(win);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setInfo(String text) {
        setInfo.accept(text);
    }

    private boolean lock = false;

    public boolean isLocked() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public int getScore() {
        return getSystem(LevelScoreSystem.class).getScore();
    }

    public Sprite getRuna() {
        return runa;
    }

}
