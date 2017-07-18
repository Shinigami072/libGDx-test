package com.mygdx.game.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.MSGame;

/**
 * Created by shinigami on 01.06.17.
 */
public class ConsoleOutputActor extends Actor {

    static String output = "";
    static int line =0;
    static BitmapFont f;
    public ConsoleOutputActor() {
        setFont(MSGame.gameSkin.getFont("default-font"));
        setColor(Color.WHITE.cpy());
        setBounds(0f,0f,100f,200f);
        clear();
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                print("breach complete.");
            }
        },0.5f);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                print("logging in...");
            }
        },1f);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                print("access granted");
            }
        },1.7f);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                print("beginning local network scan...");
            }
        },2.6f);
    }
    public static  void setFont(BitmapFont font){
        f= font;
    }
    public static void print(String s){
        output+=s+"\n";
        line++;
    }

    public static void clearConsole(){
        line=0;
        output= "";
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(getColor());
        f.draw(batch,output,getX(),getY()+(line>10?(line-10)*20f:0));
    }
}
