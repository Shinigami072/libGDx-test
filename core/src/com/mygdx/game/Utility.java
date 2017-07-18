package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.node.Connection;
import com.mygdx.game.node.Node;
import com.mygdx.game.node.NodeConnectionDictionary;

/**
 * Created by shinigami on 05.06.17.
 */
public class Utility {

    public static final String[] greek ={
      "alpha",
       "beta",
            "gamma",
            "delta",
            "epsilon",
            "zeta",
            "eta",
            "theta",
            "iota",
            "kappa",
            "lambda",
            "nu",
            "xi",
            "omnicron",
            "pi",
            "rho",
            "sigma",
            "tau",
            "upsilon",
            "phi",
            "chi",
            "psi",
            "omega"
    };
    public static String getNodeName(int deepness,int count){

        String ID = greekprefix(deepness)+"-"+count;
        Gdx.app.log("getNodeName",ID);
        return ID;

    }

    public static String getConnectionName(String ID1,boolean shorten1,String ID2,boolean shorten2){

        Gdx.app.log("getConnectionName","ID1 "+ID1+" ,ID2 "+ID2);

        if(shorten1)
            ID1=shorten(ID1);
        if(shorten2)
            ID2=shorten(ID2);
        return ID1+"-"+ID2;
    }

    private static String shorten(String ID){
        int breaker = ID.lastIndexOf("-");
        if(breaker<0)
            breaker= ID.length();
        Gdx.app.log("shortening",ID+breaker);


        String number = breaker+1>=ID.length()? "":ID.substring(breaker+1);
        ID = ID.substring(0,breaker);
        String characters = ""+ID.charAt(0);
        if(ID.split(".").length!=0) {
            characters="";
            for (String s : ID.split("."))
                characters += s.charAt(0);
        }
        Gdx.app.log("shortening",ID+":"+characters+number);

        return characters+number;

    }
    private static String greekprefix(int number){
        int c = (number/greek.length)/greek.length;
        int r = c%greek.length;
        return greekprefix(c,r, greek[number%greek.length]);
    }
    private static String greekprefix(int number,int remainder,String current){
        if(number == 0)
            return current;
        current = current +"."+ greek[remainder];

        return greekprefix(number/greek.length,number%greek.length,current);
    }

    public static void generateNodeTree(NodeConnectionDictionary ncd,int levels,int maxDeep){
        Node n = ncd.getNode("root");

        generateDown(ncd,0,n,levels,maxDeep,MathUtils.random(1,maxDeep),
                new Vector2((MathUtils.randomBoolean()?1:-1),(MathUtils.randomBoolean()?1:-1)),750f
        );

        Node parent;
        for(int i=0;i<maxDeep*levels;i++)
            if(MathUtils.randomBoolean(0.3f))
        {
            parent =null;
            n=null;
            while(parent == n){
            parent =ncd.randomNode();
            n = ncd.randomNode();
            }
            new Connection(getConnectionName(parent.ID,true,n.ID,true),parent.ID,n.ID,ncd, Connection.mode.BIDIRECTIONAL);
        }
    }
    public static void generateDown(NodeConnectionDictionary ncd,int level,Node parent,int levels,int maxDeep,int layer,Vector2 direction,float maxdist){

        if(level>=levels)
            return;
        Node n;

        int nextlayer=0;
        float angle = 360f/layer;
        direction.rotate(90f+MathUtils.random(-angle/2f,angle/2f));
        int offset =0;
        for(int c  = layer;c>0;c--)
        {
            direction.setLength(MathUtils.random(maxdist*0.9f,maxdist));
            while(ncd.getNode(getNodeName(level,c+offset)) !=null)
                offset++;
            n = new Node(getNodeName(level,c+offset),ncd);

            if(MathUtils.randomBoolean(0.6f))
                n.setLevel(n.getLevel()+1);
            if(MathUtils.randomBoolean(0.4f))
                n.setLevel(n.getLevel()+1);
            if(MathUtils.randomBoolean(0.2f))
            n.setLevel(n.getLevel()+1);
            if(MathUtils.randomBoolean(0.1f))
            n.setLevel(n.getLevel()+1);

            n.setPosition(parent.getX(),parent.getY());
            n.moveBy(direction.x,direction.y);
            nextlayer = MathUtils.random(1,maxDeep);
            generateDown(ncd,level+1,n,levels,maxDeep,nextlayer,direction.cpy(),maxdist*0.5f);
            direction.rotate(angle);

            if(MathUtils.randomBoolean())
                 new Connection(getConnectionName(parent.ID,true,n.ID,true),parent.ID,n.ID,ncd, MathUtils.randomBoolean(0.1f)?Connection.mode.UNIDIRECTIONAL:Connection.mode.BIDIRECTIONAL);
            else
                new Connection(getConnectionName(n.ID,true,parent.ID,true),n.ID,parent.ID,ncd, MathUtils.randomBoolean(0.1f)?Connection.mode.UNIDIRECTIONAL:Connection.mode.BIDIRECTIONAL);

        }
    }
}
