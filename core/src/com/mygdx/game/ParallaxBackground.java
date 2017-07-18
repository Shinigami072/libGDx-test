package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * Created by shinigami on 31.05.17.
 */
public class ParallaxBackground extends Actor{
    Vector2 speed;
    Vector2 position;

    float layerDiff = 2.0f;

    float x,y,width,heigth,scaleX,scaleY;
    int originX, originY,rotation,srcX,srcY;
    boolean flipX,flipY;


    private Array<Texture> layers;
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        position.add(speed);
        for(int i=0;i<layers.size;i++) {
            srcX = MathUtils.round(position.x + i * layerDiff * position.x);
            srcY = MathUtils.round(position.y + i * layerDiff * position.y);
            batch.draw(layers.get(i), x, y, originX, originY, width, heigth, scaleX, scaleY, rotation, srcX, srcY, layers.get(i).getWidth(), layers.get(i).getHeight(), flipX, flipY);
        }
    }

    public void setSpeed(Vector2 speed) {
        this.speed.set(speed);
    }

    public void setSpeed(float x,float y){
        speed.set(x,y);
    }

    public ParallaxBackground(Array<Texture> t){
        layers = t;

        for(Texture tex:layers)
            tex.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);

        x = y = originX = originY = rotation = srcY = 0;
        width =  Gdx.graphics.getWidth();
        heigth = Gdx.graphics.getHeight();
        scaleX = scaleY = 1;
        flipX = flipY = false;
        speed =new Vector2(0,0);
        position = new Vector2(0,0);
   }

}
