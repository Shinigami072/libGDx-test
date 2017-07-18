package com.mygdx.game.AI;

import com.mygdx.game.Owner;
import com.mygdx.game.node.Node;
import com.mygdx.game.node.NodeConnectionDictionary;

/**
 * Created by shinigami on 05.06.17.
 */
public abstract class Controller {
    protected NodeConnectionDictionary ncd;
    protected Owner owner;
    protected Node root;

    public Controller(NodeConnectionDictionary n,Node r,Owner o){
        root=r;
        owner=o;
        ncd=n;
    }

    public abstract void act(float delta);


}
