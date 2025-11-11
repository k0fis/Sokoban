package kfs.sokoban.comp;

import kfs.sokoban.ecs.KfsComp;

public class TileEffectComponent implements KfsComp {
    public enum Type { ROTATE, SLIDE, PULSE }
    public Type type;
    public float timer;
}
