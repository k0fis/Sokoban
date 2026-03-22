package kfs.sokoban.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import kfs.sokoban.KfsMain;
import kfs.sokoban.World;
import kfs.sokoban.sys.InputSystem;

public class SokobanScreen extends ScreenAdapter {

    private final KfsMain kfsGame;
    private World world;

    // World rendering
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture background;
    private final String map;

    // UI overlay
    private Stage uiStage;
    private Skin skin;
    private Label infoLabel;
    private boolean musicOn = false;

    private float gameZoon = 0.6f;

    public SokobanScreen(KfsMain kfsGame, String map) {
        this.kfsGame = kfsGame;
        this.map = map;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        world = new World(this::gameOver, this::setInfo, camera, map);
        background = new Texture(Gdx.files.internal("logo/logo-text.png"));

        uiStage = new Stage(new ScreenViewport());

        InputMultiplexer mux = new InputMultiplexer(uiStage, new InputSystem(world));
        Gdx.input.setInputProcessor(mux);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table root = new Table();
        root.setFillParent(true);
        uiStage.addActor(root);

        // Top bar
        Table topBar = new Table();
        topBar.setBackground(skin.newDrawable("white", Color.DARK_GRAY));
        topBar.pad(5);

        infoLabel = new Label("Player: 1 | Score: 0", skin);

        // Buttons
        TextButton exitBtn = new TextButton("Exit", skin);
        TextButton musicBtn = new TextButton("Music: OFF", skin);
        TextButton zoomIn = new TextButton("+", skin);
        TextButton zoomOut = new TextButton("-", skin);


        zoomIn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameZoon -= 0.01f;
                Gdx.app.log("Sokoban", "ZOOM: " + gameZoon);
            }
        });

        zoomOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameZoon += 0.01f;
                Gdx.app.log("Sokoban", "ZOOM: " + gameZoon);
            }
        });

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameOver(false);
            }
        });

        musicBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                musicOn = !musicOn;
                musicBtn.setText(musicOn ? "Music: ON" : "Music: OFF");
                kfsGame.music(musicOn);
            }
        });

        topBar.add(infoLabel).expandX().left().padRight(20);
        topBar.add(new Label(map, skin)).expandX().left().padRight(20);
        topBar.add(zoomIn).pad(5);
        topBar.add(zoomOut).pad(5);
        topBar.add(musicBtn).pad(5);
        topBar.add(exitBtn).pad(5);

        root.top().add(topBar).expandX().fillX();
    }

    @Override
    public void render(float delta) {
        // Clear
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // === WORLD ===
        world.update(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, ((world.getWidth()/2f)*World.TILE_SIZE) - (background.getWidth() / 2f),
            ((world.getHeight()/2f) *World.TILE_SIZE) - background.getHeight() / 2f,
            uiStage.getWidth()*2, uiStage.getHeight()*2);
        batch.end();

        float oldZoom = camera.zoom;
        batch.begin();
        camera.zoom = gameZoon;
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        world.render(batch);
        batch.end();
        camera.zoom = oldZoom;

        // === UI ===
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        // Keep world scaling correct
        camera.viewportWidth = width;
        camera.viewportHeight = height;

        world.getRuna().setSize(width*0.5f, height*0.5f);
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        uiStage.dispose();
        skin.dispose();
        world.dispose();
    }

    private void gameOver(boolean win) {
        if (win) {
            int score = world.getScore();
            kfsGame.setScreen(new GameOverScreen(kfsGame, score, map));
        } else {
            kfsGame.setScreen(new LevelDoneScreen(kfsGame, false, lastInfo, map));
        }
    }

    private String lastInfo = "";

    private void setInfo(String text) {
        lastInfo = text;
        infoLabel.setText(text);
    }
}
