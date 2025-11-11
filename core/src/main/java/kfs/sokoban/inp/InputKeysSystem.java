package kfs.sokoban.inp;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import kfs.sokoban.World;
import kfs.sokoban.comp.InputComponent;
import kfs.sokoban.comp.PlayerComponent;
import kfs.sokoban.ecs.Entity;
import kfs.sokoban.ecs.KfsSystem;

public class InputKeysSystem extends InputAdapter implements KfsSystem {

    private final World world;

    public InputKeysSystem(World world) {
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
}

