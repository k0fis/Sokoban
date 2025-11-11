package kfs.sokoban.sys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import kfs.sokoban.Tile;
import kfs.sokoban.World;
import kfs.sokoban.comp.*;
import kfs.sokoban.ecs.Entity;
import kfs.sokoban.ecs.KfsSystem;

public class RenderSystem implements KfsSystem {

    private final World world;

    public RenderSystem(World world) {
        this.world = world;
    }

    @Override
    public void render(SpriteBatch batch) {

        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                batch.draw(world.getTexture(Tile.FLOOR), x*World.TILE_SIZE, y*World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
            }
        }

        for (Entity e : world.getEntitiesWith(RenderComponent.class, WallComponent.class)) {
            draw(batch, e);
        }
        for (Entity e : world.getEntitiesWith(RenderComponent.class, GoalComponent.class)) {
            draw(batch, e);
        }
        for (Entity e : world.getEntitiesWith(RenderComponent.class, BoxComponent.class)) {
            draw(batch, e);
        }
        for (Entity e : world.getEntitiesWith(RenderComponent.class, PlayerComponent.class)) {
            draw(batch, e);
        }

    }

    private void draw(SpriteBatch batch, Entity e) {
        RenderComponent rc = world.getComponent(e, RenderComponent.class);

        AnimatedMovementComponent ac = world.getComponent(e, AnimatedMovementComponent.class);
        if ((ac != null) && (ac.moving)) {
            Texture texture = world.getTexture(rc.tile);
            if (texture != null) {
                batch.draw(world.getTexture(rc.tile), ac.posX, ac.posY, World.TILE_SIZE, World.TILE_SIZE);
            } else {
                Gdx.app.error("RenderSystem", "Texture not found for tile " + rc.tile.name());
            }
        } else {
            PositionComponent pc = world.getComponent(e, PositionComponent.class);
            Texture texture = world.getTexture(rc.tile);
            if (texture != null) {
                batch.draw(world.getTexture(rc.tile), pc.x * World.TILE_SIZE, pc.y * World.TILE_SIZE);
            } else {
                Gdx.app.error("RenderSystem", "Texture not found for tile " + rc.tile.name());
            }
        }

    }

}
