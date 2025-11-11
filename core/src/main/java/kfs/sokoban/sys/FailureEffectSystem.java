package kfs.sokoban.sys;

import com.badlogic.gdx.graphics.Camera;
import kfs.sokoban.World;
import kfs.sokoban.comp.FailureEffectComponent;
import kfs.sokoban.ecs.Entity;
import kfs.sokoban.ecs.KfsSystem;

public class FailureEffectSystem implements KfsSystem {

    private final World world;
    private final Camera camera;

    private float baseCameraX, baseCameraY;

    public FailureEffectSystem(World world, Camera camera) {
        this.world = world;
        this.camera = camera;
    }

    public void setBaseCamera() {
        baseCameraX = camera.position.x;
        baseCameraY = camera.position.y;

    }

    @Override
    public void update(float delta) {
        for (Entity e : world.getEntitiesWith(FailureEffectComponent.class)) {
            FailureEffectComponent fx = world.getComponent(e, FailureEffectComponent.class);

            fx.time += delta;
            float t = Math.min(fx.time / fx.duration, 1f);

            // dark overlay fade-in (quad drawn in render system)
            fx.opacity = Math.min(t * 1.8f, 0.6f);

            // quick camera shake early
            if (t < 0.35f) {
                float intensity = (1f - t / 0.35f); // fade out
                float shakeX = (float)Math.sin(fx.time * 40) * 8f * intensity;
                float shakeY = (float)Math.cos(fx.time * 35) * 8f * intensity;

                camera.position.set(
                    baseCameraX + shakeX,
                    baseCameraY + shakeY,
                    0
                );
            } else {
                camera.position.set(baseCameraX, baseCameraY, 0);
            }

            if (t >= 1f) {
                fx.finished = true;
                camera.position.set(baseCameraX, baseCameraY, 0);
            }
        }
    }
}
