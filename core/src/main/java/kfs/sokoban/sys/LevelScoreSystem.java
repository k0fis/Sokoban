package kfs.sokoban.sys;

import kfs.sokoban.World;
import kfs.sokoban.comp.*;
import kfs.sokoban.ecs.Entity;
import kfs.sokoban.ecs.KfsSystem;

import java.util.Set;
import java.util.stream.Collectors;

public class LevelScoreSystem implements KfsSystem {

    private final World world;
    private final float timeLimit = 121f;
    private float time;
    private int count;

    private final Set<PositionComponent> boxies;
    private final Set<PositionComponent> goals;
    private final PositionComponent playerPosition;

    public LevelScoreSystem(World world) {
        this.world = world;
        time = 0f;
        count = 0;
        boxies = world.getEntitiesWith(BoxComponent.class).stream().map(e->world.getComponent(e, PositionComponent.class)).collect(Collectors.toSet());
        goals = world.getEntitiesWith(GoalComponent.class).stream().map(e->world.getComponent(e, PositionComponent.class)).collect(Collectors.toSet());
        playerPosition = world.getEntitiesWith(PositionComponent.class, PositionComponent.class).stream()
            .map(e->world.getComponent(e, PositionComponent.class))
            .findAny().orElse(null);
    }

    @Override
    public void update(float delta) {
        for (Entity entity : world.getEntitiesWith(RuneEffectComponent.class)) {
            RuneEffectComponent r = world.getComponent(entity, RuneEffectComponent.class);
            if (r.finished) {
                world.gameOver(true);
                return;
            }
        }
        for (Entity entity : world.getEntitiesWith(FailureEffectComponent.class)) {
            FailureEffectComponent p = world.getComponent(entity, FailureEffectComponent.class);
            if (p.finished) {
                world.gameOver(false);
                return;
            }
        }

        time += delta;
        world.setInfo(prepareInfo());
        if (count == goals.size()) {
            world.setLock(true);
            RuneEffectComponent rc = new RuneEffectComponent();
            rc.x = playerPosition.x * World.TILE_SIZE;
            rc.y = playerPosition.y * World.TILE_SIZE;
            Entity runa = world.createEntity();
            world.addComponent(runa, rc);
        }
        if (time >= timeLimit) {
            world.setLock(true);
            world.getSystem(FailureEffectSystem.class).setBaseCamera();
            FailureEffectComponent fc = new FailureEffectComponent();
            Entity fe = world.createEntity();
            world.addComponent(fe, fc);
        }
    }

    public String prepareInfo() {
        count = 0;
        for (PositionComponent goal : goals) {
            for (PositionComponent box : boxies) {
                if (goal.equals(box)) {
                    count++;
                }
            }
        }
        return String.format("Time: %02d:%02d / %02d:%02d | Boxes: %03d/%03d",
            (int)(time / 60),
            (int)(time % 60),
            (int)(timeLimit / 60),
            (int)(timeLimit % 60),
            count,
            goals.size()
        );
    }

}
