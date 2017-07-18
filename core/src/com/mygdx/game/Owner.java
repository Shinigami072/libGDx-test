package com.mygdx.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

/**
 * Created by shinigami on 01.06.17.
 */
public class Owner {
    public Color c;
    public String ID;
    private Array<String> controlledNodes;


    public Owner( String ID,Color c){
        this.c =c;
        this.ID=ID;
        controlledNodes = new Array<String>(false,16);
    }

    public void addNode(String ID){
        controlledNodes.add(ID);
    }

    public void removeNode(String ID){
        controlledNodes.removeValue(ID,false);
    }

    public Array<String> getNodes(){
        return controlledNodes;
    }
}
