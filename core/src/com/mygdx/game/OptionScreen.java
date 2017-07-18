package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by shinigami on 25.05.17.
 */
public class OptionScreen implements Screen {

    private Game game;
    private Stage stage;
    public OptionScreen(Game msGame) {
        game = msGame;
        stage = new Stage(new ScreenViewport());
        Gdx.app.log("CREATR","GameScreen");

        Label title = new Label("Options SCREEN",MSGame.gameSkin);
        title.setAlignment(Align.center);
        title.setFontScale(1.5f);
        title.setColor(MSGame.gameSkin.getColor("black"));
        title.setY(2/3f*Gdx.graphics.getHeight());
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        TextButton backButton = new TextButton("Back",MSGame.gameSkin);
        backButton.setWidth(Gdx.graphics.getWidth()/2);
        backButton.setHeight(40f);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        backButton.setPosition(
                Gdx.graphics.getWidth()/2-backButton.getWidth()/2
                ,Gdx.graphics.getHeight()/4-backButton.getHeight()/2);
        stage.addActor(backButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
