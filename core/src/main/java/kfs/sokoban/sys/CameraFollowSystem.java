package kfs.sokoban.sys;

import com.badlogic.gdx.graphics.Camera;
import kfs.sokoban.World;
import kfs.sokoban.comp.PositionComponent;
import kfs.sokoban.ecs.KfsSystem;

public class CameraFollowSystem implements KfsSystem {

    private final World world;
    private final Camera camera;
    public PositionComponent cameraEntityPosition;
    private float lerp = 6f; // higher = snappier

    public CameraFollowSystem(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
        this.cameraEntityPosition = null;
    }

    @Override
    public void update(float delta) {
        if (cameraEntityPosition != null) {
            float targetX = cameraEntityPosition.x * World.TILE_SIZE;
            float targetY = cameraEntityPosition.y * World.TILE_SIZE;
            camera.position.x += (targetX - camera.position.x) * Math.min(1f, lerp * delta);
            camera.position.y += (targetY - camera.position.y) * Math.min(1f, lerp * delta);
            camera.update();
        }
    }
}
