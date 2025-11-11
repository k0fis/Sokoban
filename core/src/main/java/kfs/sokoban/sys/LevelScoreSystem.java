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

    private final Set<PositionComponent> boxes;
    private final Set<PositionComponent> goals;
    private final Set<PositionComponent> walls;
    private final PositionComponent playerPosition;

    public LevelScoreSystem(World world) {
        this.world = world;
        time = 0f;
        count = 0;
        boxes = world.getEntitiesWith(BoxComponent.class).stream().map(e->world.getComponent(e, PositionComponent.class)).collect(Collectors.toSet());
        goals = world.getEntitiesWith(GoalComponent.class).stream().map(e->world.getComponent(e, PositionComponent.class)).collect(Collectors.toSet());
        walls = world.getEntitiesWith(WallComponent.class).stream().map(e->world.getComponent(e, PositionComponent.class)).collect(Collectors.toSet());
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
        boolean failed = time >= timeLimit;
        if (!failed) failed = hasCornerDeadlock();
        if (!failed) failed = has2x2Deadlock();

        if (failed) {
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
            for (PositionComponent box : boxes) {
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


    boolean hasCornerDeadlock() {
        for (PositionComponent b : boxes) {
            if (isPosition(b, goals))
                continue;
            boolean up = isWall(b.x, b.y-1);
            boolean down = isWall(b.x, b.y+1);
            boolean left = isWall(b.x-1, b.y);
            boolean right = isWall(b.x+1, b.y);
            if ((up&&left)||(up&&right)||(down&&left)||(down&&right))
                return true;
        }
        return false;
    }

    boolean isBox(int x, int y) {
        return isPosition(new PositionComponent(x, y), boxes);
    }
    boolean isBox(PositionComponent p) {
        return isPosition(p, boxes);
    }
    boolean isWall(int x, int y) {
        return isPosition(new PositionComponent(x, y), walls);
    }
    boolean isPosition(PositionComponent p, Set<PositionComponent> set) {
        for (PositionComponent w : set) {
            if (w.equals(p)) {
                return true;
            }
        }
        return false;
    }

    boolean has2x2Deadlock() {
        for (PositionComponent b : boxes) {
            for (int dx = -1; dx <= 0; dx++)
                for (int dy = -1; dy <= 0; dy++) {
                    PositionComponent p1=new PositionComponent(b.x+dx,   b.y+dy);
                    PositionComponent p2=new PositionComponent(b.x+dx+1, b.y+dy);
                    PositionComponent p3=new PositionComponent(b.x+dx,   b.y+dy+1);
                    PositionComponent p4=new PositionComponent(b.x+dx+1, b.y+dy+1);
                    if (isBox(p1)&&isBox(p2)&&isBox(p3)&&isBox(p4))
                        if (!isPosition(p1, goals)&&!isPosition(p2, goals)&&!isPosition(p3, goals)&&!isPosition(p4, goals))
                            return true;
                }
        }
        return false;
    }


}
