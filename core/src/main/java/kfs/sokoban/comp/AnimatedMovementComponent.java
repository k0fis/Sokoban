package kfs.sokoban.comp;

import kfs.sokoban.World;
import kfs.sokoban.ecs.KfsComp;

public class AnimatedMovementComponent implements KfsComp {
    public enum EasingType {SMOOTHSTEP, CUBIC, QUADRATIC, LINEAR}

    public float startX, startY;
    public float posX, posY;
    public float targetX, targetY;
    public float progress = 0;          // 0..1
    public float speed = 6f;        // dlaždic za sekundu
    public EasingType easing = EasingType.SMOOTHSTEP;

    public boolean moving = false;


    public void setMove(int fromX, int fromY, int toX, int toY) {
        startX = fromX * World.TILE_SIZE;
        startY = fromY * World.TILE_SIZE;
        targetX = toX * World.TILE_SIZE;
        targetY = toY * World.TILE_SIZE;
        posX = startX;
        posY = startY;
        progress = 0;
        moving = true;
    }
}
