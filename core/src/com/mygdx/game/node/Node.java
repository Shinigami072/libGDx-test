package com.mygdx.game.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MSGame;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Owner;
import com.mygdx.game.Textures;

import java.util.ArrayList;

/**
 * Created by shinigami on 01.06.17.
 */
public class Node extends Actor {




    public enum State {
        NEUTRAL,
        OWNED,
        REINFORCING,
        CONTESTED;
    }

    public static enum Type {
        Normal,
        Entry,
        Exit,
        L_1,
        L_2,
        L_3;
    }

    private NodeConnectionDictionary dict;
    public final String ID;
    public String OWNER;
    public State state;
    public String contestant = null;
    private float progress =0f;
    private Array<String> connections;
    public Type type;
    private int level;
    private final String PROGRESS_BAR = "======================";
    public int getLevel(){
        return level;
    }
    public void setLevel(int l){
        level = l;
    }
    public Node(String cID,NodeConnectionDictionary cDict){
        super();
        this.ID =cID;
        this.dict= cDict;
        connections = new Array<String>();
        dict.addNode(ID,this);
        setBounds(0f,0f,64f,64f);
        setOrigin(32f,32f);
        type = Type.Normal;
        state=State.NEUTRAL;
        level =1;
        addCaptureListener(new ActorGestureListener(){

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.log("TOUCHEDNODE:",OWNER+":"+contestant+ID);
               if(hit(x,y,true)!=null)
               contest("player");
            }

            @Override
            public boolean longPress(Actor actor, float x, float y) {
                Gdx.app.log("pressedNODE:",OWNER+":"+contestant+ID);
                reinforce("player");
                return super.longPress(actor, x, y);
            }


        });
    }

    public Node(String ID,NodeConnectionDictionary dict,Type type){
        this(ID,dict);
        this.type=type;
    }

    public Node(String ID, NodeConnectionDictionary dict, Type type, String owner) {
        this(ID,dict,type);
        setOwner(owner);
    }
    public boolean isContestable(String contest){

        if(state==State.OWNED && OWNER.equals(contest))
            return false;

        return isConnected(contest);

    }

    public boolean isConnected(String connect){
        boolean isConnected = connect.equals(OWNER);
        String temp;
        for(String connection:connections)
            if(!isConnected) {
                temp = dict.getConnection(connection).getOrigin(ID);
                if(temp != null){
                    if(dict.getNode(temp).state == State.NEUTRAL)
                        continue;
                    temp = dict.getNode(temp).OWNER;

                    if(temp != null)
                        isConnected = temp.equals(connect);}
            }

        return isConnected;
    }
    public void contest(String contest){
        if(state==State.CONTESTED)
            return;

        if(isContestable(contest)){
            setState(State.CONTESTED);
            contestant = contest;
        }
    }
    public boolean isReinforcable(String ID){
        if(state==State.CONTESTED || state==State.NEUTRAL || state ==State.REINFORCING)
            return false;
        return isConnected(ID);
    }
    public void reinforce(String ID){
        if(isReinforcable(ID)){
            setState(State.REINFORCING);
        }
    }
    public void setOwner(String ID){

        if(state != State.NEUTRAL && OWNER != null) {
            if (OWNER.equals(ID))
                return;
            dict.getOwner(OWNER).removeNode(this.ID);
        }

        OWNER = ID;
        if(ID == null){
            state= State.NEUTRAL;
            return;
        }
            state = State.OWNED;
        dict.getOwner(OWNER).addNode(this.ID);

    }

    public void addConnection(String ID){
        connections.add(ID);
    }

    public void removeConnection(String ID){
        connections.removeValue(ID,false);
    }

    public Array<String> getConnections(){
        return connections;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(state != State.NEUTRAL)
        {
            Owner oldO = dict.getOwner(OWNER);
            Owner newO = dict.getOwner(contestant);

            Color oldC;
            if (oldO == null)
                oldC = Color.WHITE.cpy();
            else
                oldC = oldO.c.cpy();

            Color newC;
            if (newO == null)
                newC = Color.WHITE;
            else
                newC = newO.c;

            batch.setColor(oldC.lerp(newC, progress));
        }
        else
            batch.setColor(Color.WHITE);

        switch(type) {
            case Entry:
                batch.draw(Textures.NODE_ENTRY, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            break;
            case Exit:
                batch.draw(Textures.NODE_EXIT, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            break;
            case L_1:
                batch.draw(Textures.NODE_CUBE, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            break;
            case L_2:
                batch.draw(Textures.NODE_DB, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
            break;
            default:
            batch.draw(Textures.NODE_STACK, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }

        batch.setColor(0f,0.5f,0f,1f);

        //if(isContestable("player"))
        MSGame.gameSkin.getFont("default-font").draw(batch,ID+" lv. "+level,getX(),getY()+2f);

        if(progress>0f) {
            MSGame.gameSkin.getFont("default-font").draw(batch,PROGRESS_BAR.substring(0,MathUtils.roundPositive(progress * (ID+""+level).length())), getX(), getY() - 20f);
            MSGame.gameSkin.getFont("default-font").draw(batch, MathUtils.roundPositive(progress * 100) + "%", getX()+28f, getY() - 40f);
        }
        batch.setColor(1f,1f,1f,1f);
    }
    public void setState(State s){
        state = s;
        contestant=null;
        progress = 0f;
    }
    public void resetState(){
        if(OWNER==null)
            setState(State.NEUTRAL);
        else
            setState(State.OWNED);

    }
    @Override
    public void act(float delta) {
        super.act(delta);

        switch(state){
            case CONTESTED: {
                if (!isContestable(contestant)) {
                    resetState();
                    break;
                }

                if (MathUtils.random() < 1f / (level * level / 2)) {
                    progress += 0.05 * (delta / 0.1);
//                    if (MathUtils.random() < 0.01f)
//                        MyGdxGame.console.print("Hacking protocol on Node '" + ID + "' " + MathUtils.roundPositive(progress * 100) + "% Complete");
                }

                if (progress >= 1f) {
                    setOwner(contestant);
                    setState(State.OWNED);
                    MyGdxGame.console.print("Node '" + ID + "' has been taken over");
                }

                break;
            }

            case REINFORCING:{
                if (MathUtils.random() < (1f / (3*level))) {
                    progress += 0.1f;
                }

                if (progress >= 1f) {
                    level++;
                    resetState();
                    MyGdxGame.console.print("Node '" + ID + "' has been reinforced");
                }
                break;
            }
        }


    }

    @Override
    public String toString() {
        return ID+":NODE:"+state+level;
    }
}
