package kfs.sokoban.sys;

import kfs.sokoban.Tile;
import kfs.sokoban.World;
import kfs.sokoban.comp.*;
import kfs.sokoban.ecs.Entity;
import kfs.sokoban.ecs.KfsSystem;

import java.util.ArrayList;
import java.util.List;

public class MovementSystem implements KfsSystem {

    private final World world;

    public MovementSystem(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        if (world.isLocked()) {
            return;
        }
        for (Entity player : world.getEntitiesWith(PlayerComponent.class, InputComponent.class, PositionComponent.class)) {
            AnimatedMovementComponent pm = world.getComponent(player, AnimatedMovementComponent.class);
            if (pm == null) {
                pm = new AnimatedMovementComponent();
                world.addComponent(player, pm);
            }
            if (pm.moving) continue;

            InputComponent ic = world.getComponent(player, InputComponent.class);

            if (ic.dx == 0 && ic.dy == 0) continue;

            PositionComponent pt = world.getComponent(player, PositionComponent.class);

            int tx = pt.x + ic.dx;
            int ty = pt.y + ic.dy;

            // update render
            RenderComponent rc = world.getComponent(player, RenderComponent.class);
            if (rc != null) {
                if (ic.dx == 1) rc.tile = Tile.PLAYER_R;
                if (ic.dx == -1) rc.tile = Tile.PLAYER_L;
                if (ic.dy == 1) rc.tile = Tile.PLAYER_U;
                if (ic.dy == -1) rc.tile = Tile.PLAYER_D;
            }

            boolean isBlocking = false;
            Entity box = null;
            for (Entity blockingBlock : getEntityAt(tx, ty)) {
                if (world.getComponent(blockingBlock, WallComponent.class) != null) {
                    isBlocking = true;
                }
                if (world.getComponent(blockingBlock, BoxComponent.class) != null) {
                    box = blockingBlock;
                }
            }
            if (isBlocking) {
                cleanUpMove(ic);
                continue;
            }

            if (box != null) {
                PositionComponent bt = world.getComponent(box, PositionComponent.class);

                int bx2 = bt.x + ic.dx;
                int by2 = bt.y + ic.dy;

                isBlocking = false;
                for (Entity blockingBlock : getEntityAt(bx2, by2)) {
                    if (world.getComponent(blockingBlock, WallComponent.class) != null
                        || world.getComponent(blockingBlock, BoxComponent.class) != null) {
                        isBlocking = true;
                        break;
                    }
                }
                if (isBlocking) {
                    cleanUpMove(ic);
                    continue;
                }
                AnimatedMovementComponent bm = world.getComponent(box, AnimatedMovementComponent.class);
                if (bm == null) {
                    bm = new AnimatedMovementComponent();
                    world.addComponent(box, bm);
                }
                bt.x = bx2;
                bt.y = by2;
                bm.setMove(bt.x - ic.dx, bt.y - ic.dy, bt.x, bt.y);
            }

            pt.x = tx;
            pt.y = ty;

            pm.setMove(tx - ic.dx, ty - ic.dy, tx, ty);

            cleanUpMove(ic);
        }
    }

    public void cleanUpMove(InputComponent ic) {
        ic.dx = 0;
        ic.dy = 0;
    }

    private List<Entity> getEntityAt(int tx, int ty) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity e : world.getEntitiesWith(PositionComponent.class)) {
            PositionComponent pt = world.getComponent(e, PositionComponent.class);
            if (pt.x == tx && pt.y == ty) {
                entities.add(e);
            }
        }
        return entities;
    }
}
