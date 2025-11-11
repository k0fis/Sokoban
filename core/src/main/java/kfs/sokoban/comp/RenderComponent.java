package kfs.sokoban.comp;

import kfs.sokoban.Tile;
import kfs.sokoban.ecs.KfsComp;

public class RenderComponent implements KfsComp {

    public Tile tile;

    public RenderComponent(Tile tile) {
        this.tile = tile;
    }
}
