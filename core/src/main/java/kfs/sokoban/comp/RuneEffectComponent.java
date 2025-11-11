package kfs.sokoban.comp;

import kfs.sokoban.ecs.KfsComp;

public class RuneEffectComponent implements KfsComp {

    public float x;
    public float y;

    public float time = 0f;
    public float duration = 1.8f;

    public float startScale = 0.4f;
    public float endScale = 1.3f;
    public float scale = 0.4f;

    public float opacity = 0.45f;

    public boolean finished = false;
}
