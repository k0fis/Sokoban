package kfs.sokoban;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import kfs.sokoban.outp.MusicManager;
import kfs.sokoban.ui.MainScreen;
import kfs.sokoban.ui.SokobanScreen;

import java.util.List;
import java.util.stream.Stream;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class KfsMain extends Game {

    private MusicManager music;

    @Override
    public void create() {
        music = new MusicManager("music");
        setScreen(new MainScreen(this));
    }

    public void music(boolean play) {
        if (play) {
            music.play();
        } else {
            music.stop();
        }
    }

    public List<FileHandle> getMaps() {
        FileHandle levelDir = Gdx.files.internal("maps");
        if (!levelDir.exists() || !levelDir.isDirectory()) {
            Gdx.app.log("KfsMain", "No levels found in /assets/maps/");
            return List.of();
        }

        return Stream.of(levelDir.list())
            //.filter(file -> file.name().endsWith(".txt"))
            .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
            .toList();
    }

    public String getMap(String before) {
        String prev = "";
        for (FileHandle file : getMaps()) {
            if (before.equals(prev)) {
                return file.path();
            }
            prev = file.path();
        }
        Gdx.app.log("KfsMain", "No next map after " + before + " found");
        return null;
    }
}
