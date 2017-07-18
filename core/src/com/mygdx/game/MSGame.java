package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by shinigami on 25.05.17.
 */
public class MSGame extends Game {

    public static Skin gameSkin;

    @Override
    public void create() {
        Textures.NODE_STACK = new TextureRegion(new Texture(Gdx.files.internal("node/stack.png")));
        Textures.NODE_DB = new TextureRegion(new Texture(Gdx.files.internal("node/database.png")));
        Textures.NODE_EXIT= new TextureRegion(new Texture(Gdx.files.internal("node/exit-door.png")));
        Textures.NODE_ENTRY = new TextureRegion(new Texture(Gdx.files.internal("node/entry-door.png")));
        Textures.NODE_CUBE = new TextureRegion(new Texture(Gdx.files.internal("node/cube.png")));

        Gdx.app.log("CREATR","GameScreen");
        gameSkin = new Skin(Gdx.files.internal("uiskin.json"));
        setScreen( new TitleScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
