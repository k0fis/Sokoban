package kfs.sokoban.sys;

import kfs.sokoban.World;
import kfs.sokoban.comp.AnimatedMovementComponent;
import kfs.sokoban.ecs.Entity;
import kfs.sokoban.ecs.KfsSystem;

public class MovementAnimationSystem implements KfsSystem {

    private final World world;

    public MovementAnimationSystem(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        for (Entity e : world.getEntitiesWith(AnimatedMovementComponent.class)) {
            AnimatedMovementComponent amc = world.getComponent(e, AnimatedMovementComponent.class);
            if (amc.moving) {
                amc.progress += delta * amc.speed;
                if (amc.progress >= 1f) {
                    amc.progress = 1f;
                    amc.posX = amc.targetX;
                    amc.posY = amc.targetY;
                    amc.moving = false;
                }

                float t = applyEasing(amc.easing, amc.progress);

                amc.posX = amc.startX + (amc.targetX - amc.startX) * t;
                amc.posY = amc.startY + (amc.targetY - amc.startY) * t;
            }
        }
    }

    private float applyEasing(AnimatedMovementComponent.EasingType type, float t) {
        return switch (type) {
            case SMOOTHSTEP -> t * t * (3 - 2 * t);
            case CUBIC -> t * t * t;
            case QUADRATIC -> t * t;
            default -> t;
        };
    }
}
