package com.mygdx.game.node;

import com.badlogic.gdx.Gdx;

import java.util.Enumeration;

/**
 * Created by shinigami on 01.06.17.
 */
public class Connection {
    public enum mode{
        BIDIRECTIONAL,
        UNIDIRECTIONAL;
    };
    private NodeConnectionDictionary dict;
    final String ID;
    private String Origin;
    private String Destination;
    public mode Mode;
    public Connection(String ID,String Origin,String Destination,NodeConnectionDictionary dict){
        this.ID =ID;
        this.dict= dict;
        this.Origin=Origin;
        this.Destination=Destination;
        this.Mode = mode.BIDIRECTIONAL;
        dict.addConnection(ID,this);
        dict.getNode(Origin).addConnection(ID);
        dict.getNode(Destination).addConnection(ID);

    }
    public Connection(String ID,String Origin,String Destination,NodeConnectionDictionary dict,mode Mode){
        this(ID,Origin,Destination,dict);
        setMode(Mode);
        Gdx.app.log("Connection",ID+""+Mode);

    }

    @Override
    public String toString() {
        return "["+ID+"]"+Origin+"-->"+Destination;
    }

    public void setMode(mode mode) {
        Mode = mode;
    }

    public String getOrigin() {
        return Origin;
    }

    public String getDestination() {
        return Destination;
    }

    public String getDestination(String Origin) {
        if(Origin.equals(this.Origin))
            return Destination;
        if(Origin.equals(Destination) && Mode == mode.BIDIRECTIONAL)
            return this.Origin;

        return null;

    }

    public String getOrigin(String Destination) {
        if(Destination.equals(this.Origin) && Mode == mode.BIDIRECTIONAL)
            return this.Destination;
        if(Destination.equals(this.Destination) )
            return this.Origin;
        return null;

    }

    public Boolean isTraversibleFrom(String client){
        if(Mode.BIDIRECTIONAL == Mode)
        return client.equals(Origin)||client.equals(Destination);
        else
            return client.equals(Origin);
    }
}
