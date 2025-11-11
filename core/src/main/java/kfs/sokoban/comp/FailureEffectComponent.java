package kfs.sokoban.comp;

import kfs.sokoban.ecs.KfsComp;

public class FailureEffectComponent implements KfsComp {
    public float time = 0f;
    public float duration = 1.55f;

    public float opacity = 0f;     // dark overlay
    public float shake = 0f;       // camera shake intensity

    public boolean finished = false;
}
