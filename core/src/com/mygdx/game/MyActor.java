package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by shinigami on 23.05.17.
 */
public class MyActor extends Actor {
    Texture img = new Texture("stock-photo-33531251.jpg");

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(img,0,0);
    }

}
