package kfs.sokoban.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import kfs.sokoban.KfsMain;

public class LevelDoneScreen extends BaseScreen {

    private final String text;
    private final boolean win;
    private final String mapName;

    public LevelDoneScreen(KfsMain game, boolean win, String text, String mapName) {
        super(game, false);
        this.text = text;
        this.win = win;
        this.mapName = mapName;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(5);
        stage.addActor(table);

        Label title = new Label(win?"YOU WIN!":"GAME OVER", new Label.LabelStyle(fontBig, Color.BROWN));
        table.add(title).padBottom(20).row();
        title.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(0.5f), Actions.fadeIn(0.5f))));

        title = new Label(mapName, new Label.LabelStyle(fontSmall, Color.BROWN));
        table.add(title).padBottom(20).row();

        Label stats = new Label(text, new Label.LabelStyle(fontMiddle, Color.BROWN));
        table.add(stats).padBottom(40).row();

        var bstyle = getTextButtonStyle(fontMiddle, Color.BROWN);

        TextButton next = new TextButton(win?"Next Level":"Retry level", bstyle);
        next.getColor().a = 0.75f;
        next.setWidth(200);
        next.setHeight(20);
        String nextLevel = game.getMap(mapName);
        if (win && nextLevel != null) {
            next.addListener(e -> {
                if (next.isPressed()) {
                    game.setScreen(new SokobanScreen(game, nextLevel));
                }
                return false;
            });
        } else {
            next.addListener(e -> {
                if (next.isPressed()) {
                    game.setScreen(new SokobanScreen(game, mapName));
                }
                return false;
            });
        }
        TextButton menu = new TextButton("Main Menu", bstyle);
        menu.getColor().a = 0.75f;
        menu.addListener(e -> {
            if (menu.isPressed()) {
                game.setScreen(new MainScreen(game));
            }
            return false;
        });

        HorizontalGroup buttons = new HorizontalGroup();
        buttons.space(20);
        if (nextLevel != null) {
            buttons.addActor(next);
        }
        buttons.addActor(menu);

        table.add(buttons);
    }
}
