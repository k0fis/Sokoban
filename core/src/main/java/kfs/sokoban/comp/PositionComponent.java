package kfs.sokoban.comp;

import kfs.sokoban.ecs.KfsComp;

public class PositionComponent implements KfsComp {
    public int x;
    public int y;

    public PositionComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PositionComponent that)) return false;

        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
