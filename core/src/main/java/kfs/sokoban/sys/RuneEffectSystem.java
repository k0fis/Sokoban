package kfs.sokoban.sys;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import kfs.sokoban.World;
import kfs.sokoban.comp.RuneEffectComponent;
import kfs.sokoban.ecs.Entity;
import kfs.sokoban.ecs.KfsSystem;

public class RuneEffectSystem implements KfsSystem {

    private final World world;

    public RuneEffectSystem(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        for (Entity e : world.getEntitiesWith(RuneEffectComponent.class)) {
            RuneEffectComponent r = world.getComponent(e, RuneEffectComponent.class);

            r.time += delta;

            float tNorm = Math.min(r.time / r.duration, 1f);

            // cubic-out easing
            float ease = 1 - (float)Math.pow(1 - tNorm, 2);

            // scale
            r.scale = r.startScale + (r.endScale - r.startScale) * ease;

            // opacity fade
            r.opacity = 1f - ease;

            // draw
            Sprite runa = world.getRuna();
            runa.setScale(r.scale);
            runa.setColor(1f, 1f, 1f, r.opacity);
            runa.setCenter(r.x, r.y);

            if (tNorm >= 1f) {
                r.finished = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Entity e : world.getEntitiesWith(RuneEffectComponent.class)) {
            RuneEffectComponent r = world.getComponent(e, RuneEffectComponent.class);
            if (!r.finished) {
                world.getRuna().draw(batch);
            }
        }
    }
}
