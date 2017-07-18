package com.mygdx.game.AI.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AI.Controller;
import com.mygdx.game.Owner;
import com.mygdx.game.node.ConsoleOutputActor;
import com.mygdx.game.node.Node;
import com.mygdx.game.node.NodeConnectionDictionary;

/**
 * Created by shinigami on 05.06.17.
 */
public class StateController extends Controller {


    enum State{
        EXPAND,
        DEFEND,
        BOLSTER,
        COUNTER;

    }

    private Array<State> state;
    private Array<String> visibleNodes;
    private float actionCooldown;
    private float dataupdatetimer;


    public StateController(NodeConnectionDictionary n, Node r, Owner o) {
        super(n, r, o);
        state = new Array<State>();
        visibleNodes = new Array<String>(false,16);
        actionCooldown = 0f;
        pushState(State.EXPAND);
    }

    public void pushState(State s){
        if(getState() != s)
        state.add(s);
    }

    public State pop(){
        return state.pop();
    }

    public State getState(){
        if(state.size == 0)
            return null;
       // Gdx.app.log("AI",owner.ID+"|"+highestlevel+"|conf:"+confidence+"|threat:"+threat+":"+state);
        return state.peek();
    }

    public void popState(State s){
        state.pop();
        state.add(s);
    }

    private void scanAllNodes(){
        Node n;
        for(String n_ID:owner.getNodes()){
            n = ncd.getNode(n_ID);
            scanNode(n);
        }
    }

    private void scanNode(Node n){
        String s;
        for(String c_ID:n.getConnections()){
            s = ncd.getConnection(c_ID).getDestination(n.ID);
            if(s !=null && !visibleNodes.contains(s,false) && !owner.getNodes().contains(s,false))
                visibleNodes.add(s);
        }
    }


    @Override
    public void act(float delta) {
        dataupdatetimer -= delta;

        if(actionCooldown>0){

        actionCooldown-=delta;
        return;
        }

        actionCooldown = 0f;

        if(getState() == null)
            pushState(State.EXPAND);


        actionCooldown +=1.1f;

        for(String nId:owner.getNodes()){
            Node n=ncd.getNode(nId);
            if(n.getConnections().size/2f>n.getLevel()){
                pushState(State.BOLSTER);
                break;
            }

            if(n.state == Node.State.CONTESTED && owner.ID.equals(n.OWNER))
            {
                pushState(State.DEFEND);
                break;
            }
        }

        visibleNodes.clear();


        switch(getState()){
            case EXPAND: {
                ConsoleOutputActor.print(owner.ID+" EXPAND"+state);
                scanAllNodes();
                visibleNodes.removeAll(owner.getNodes(),false);
                visibleNodes.shuffle();
                Node n;
                Node target = null;

                for(String nId:visibleNodes){
                    n=ncd.getNode(nId);
                    if(target == null || n.getConnections().size/(n.getLevel())> target.getConnections().size/target.getLevel())
                        target = n;

                }

                if(target != null)
                    target.contest(owner.ID);
                else
                    pop();

            }break;

            case BOLSTER:{
                ConsoleOutputActor.print(owner.ID+" BOLSTER"+state);
                visibleNodes.addAll(owner.getNodes());
                visibleNodes.shuffle();

                Node n;
                Node target = null;

                for(String nId:visibleNodes){
                    n=ncd.getNode(nId);
                    if((target == null || n.getConnections().size/n.getLevel()> target.getConnections().size/target.getLevel()) && n.getConnections().size>n.getLevel())
                        target = n;
                }

                if(target != null)
                    target.reinforce(owner.ID);
                else
                    pop();
            }break;

            case COUNTER: {

            }break;

            case DEFEND:{
                ConsoleOutputActor.print(owner.ID+" DEFEND"+state);
                scanNode(root);
                visibleNodes.shuffle();

                Node n;
                for(String nId:visibleNodes){
                    n=ncd.getNode(nId);

                    if(n.state == Node.State.CONTESTED)
                        visibleNodes.removeValue(n.ID,false);
                    if(owner.ID.equals(n.OWNER))
                        visibleNodes.removeValue(n.ID,false);
                }

                Node target = null;
                for(String nId:visibleNodes){
                    n=ncd.getNode(nId);
                    if(target == null || n.getConnections().size/n.getLevel()> target.getConnections().size/target.getLevel())
                        target = n;

                }

                pop();
                }break;
        }

    }


}
