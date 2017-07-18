package com.mygdx.game.node;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.game.Owner;

import java.util.HashMap;

/**
 * Created by shinigami on 01.06.17.
 */
public class NodeConnectionDictionary extends Group{
    private ArrayMap<String,Node> nodes;
    private ArrayMap<String,Connection> connections;
    private ArrayMap<String,Owner> owners;

    private ShapeRenderer shp;

    public NodeConnectionDictionary(Owner o){
        super();
        nodes = new ArrayMap<String, Node>();
        connections = new ArrayMap<String,Connection>();
        owners = new ArrayMap<String, Owner>();
        addOwner(o);
        addOwner(new Owner("CPU", Color.RED));
        new Node("root",this,Node.Type.Entry,o.ID);
        shp = new ShapeRenderer();

    }

    public Node getNode(String key){
        return nodes.get(key);
    }

    public void addNode(String key ,Node n){
        if(!nodes.containsKey(key) && !nodes.containsValue(n,false)){
            nodes.put(key,n);
            addActor(n);
        }
    }
    public Node lastNode(){
        return nodes.peekValue();
    }

    public Node randomNode(){
        int i=MathUtils.random(0,nodes.size-1);

        Gdx.app.log("randomNode",i+"-"+nodes.getValueAt(i));
        return nodes.getValueAt(i);
    }



    public void removeNode(String key){
        if(nodes.containsKey(key)){
            removeActor(nodes.removeKey(key));
        }
    }

    public boolean containsOwner(String ID){
        return owners.containsKey(ID);
    }

    public void addOwner(Owner o){
        if(!owners.containsKey(o.ID) && !owners.containsValue(o,false)){
            owners.put(o.ID,o);
        }
    }

    public Owner getOwner(String id){
        return owners.get(id);
    }

    public void removeOwner(Owner o){
        if(owners.containsKey(o.ID)){
            owners.removeKey(o.ID);
        }
    }

    public Connection getConnection(String key){
        return connections.get(key);
    }

    public void addConnection(String key,Connection c){
        if(!connections.containsKey(key))
            connections.put(key,c);
    }

    public void removeConnection(String key){
        if(connections.containsKey(key))
            connections.removeKey(key);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shp.begin(ShapeRenderer.ShapeType.Filled);
        Node origin ;Node dest ;
        for(Connection c:connections.values()){
            origin = getNode(c.getOrigin());
            dest = getNode(c.getDestination());

            if(origin.OWNER!=null)
            shp.setColor(getOwner(origin.OWNER).c);
            else
            shp.setColor(c.Mode== Connection.mode.BIDIRECTIONAL?Color.WHITE:Color.GRAY);
            float middleX = getX()+ origin.getOriginX() + (origin.getX()+dest.getX())/2f;
            float middleY = getY()+ origin.getOriginY() + (origin.getY()+dest.getY())/2f;

            if(origin.OWNER!=null)
                shp.setColor(getOwner(origin.OWNER).c);
            else
                shp.setColor(Color.WHITE);
            shp.rectLine(getX()+origin.getX()+origin.getOriginX(),getY()+origin.getY()+origin.getOriginY(),middleX,middleY,5f);

            if(c.Mode== Connection.mode.BIDIRECTIONAL){
            if(dest.OWNER!=null )
                shp.setColor(getOwner(dest.OWNER).c);
            else
                shp.setColor(Color.WHITE);
            }
            else
            shp.setColor(Color.DARK_GRAY);

            shp.rectLine(middleX,middleY,getX()+dest.getX()+dest.getOriginX(),getY()+dest.getY()+dest.getOriginY(),5f);

        }
        shp.end();
        batch.begin();
        super.draw(batch, parentAlpha);

    }

    public String DEBUBG(){
        String out = "NODES:\n";
        for(Node k:nodes.values)
            out+= k.ID+":"+k+"\n";

        out+= "Connection:\n";
        for(Connection k:connections.values)
            out+= k.ID+":"+k+"\n";
        return out;
    }


}
