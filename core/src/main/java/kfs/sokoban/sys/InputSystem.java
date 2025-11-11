package kfs.sokoban.sys;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import kfs.sokoban.World;
import kfs.sokoban.comp.InputComponent;
import kfs.sokoban.comp.PlayerComponent;
import kfs.sokoban.ecs.Entity;
import kfs.sokoban.ecs.KfsSystem;

public class InputSystem extends InputAdapter implements KfsSystem {

    private final World world;
    private final Vector2 start = new Vector2();

    public InputSystem(World world) {
        this.world = world;
    }

    @Override
    public boolean keyDown(int keycode) {
        for (Entity player : world.getEntitiesWith(PlayerComponent.class)) {
            InputComponent ic = world.getComponent(player, InputComponent.class);
            if (ic == null) {
                ic = new InputComponent();
                world.addComponent(player, ic);
            }
            if (keycode == Input.Keys.LEFT) {
                ic.dx = -1;
            }
            if (keycode == Input.Keys.RIGHT) {
                ic.dx = 1;
            }
            if (keycode == Input.Keys.UP) {
                ic.dy = 1;
            }
            if (keycode == Input.Keys.DOWN) {
                ic.dy = -1;
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        start.set(x, y);
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        float dx = x - start.x;
        float dy = y - start.y;

        for (Entity player : world.getEntitiesWith(PlayerComponent.class)) {
            InputComponent ic = world.getComponent(player, InputComponent.class);
            if (ic == null) {
                ic = new InputComponent();
                world.addComponent(player, ic);
            }

            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 50) ic.dx = 1;
                if (dx < -50) ic.dx = -1;
                ic.dx = 0;
            } else {
                if (dy > 50) ic.dy = -1;
                if (dy < -50) ic.dy = 1;
                ic.dy = 0;
            }
        }

        return true;
    }

}

