package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MSGame;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.node.Connection;
import com.mygdx.game.node.Node;
import com.mygdx.game.node.NodeConnectionDictionary;

import java.util.ArrayList;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "graphicsdemo";
		cfg.width = 1280;
		cfg.height = 720;
		new LwjglApplication(new MSGame(), cfg);

//		NodeConnectionDictionary ncd = new NodeConnectionDictionary();
//		new Node("alfa-1",ncd);
//		new Node("alfa-2",ncd);
//		new Node("beta-1",ncd);
//
//		new Connection("r-a1","root","alfa-1",ncd).setMode(Connection.mode.UNIDIRECTIONAL);
//		new Connection("r-a2","root","alfa-2",ncd);
//		new Connection("a1-b1","alfa-1","beta-1",ncd).setMode(Connection.mode.UNIDIRECTIONAL);
//
//		Gdx.app.log("NODE",ncd.DEBUBG());
//		Gdx.app.log("NODE",""+ncd.getNode("root"));
//		visited = new ArrayList<String>();
//		traverse(ncd,"root");

	}

	private static ArrayList<String> visited;
	public static void traverse(NodeConnectionDictionary ncd,String ID){
		if(ID ==null)
			return;
		visited.add(ID);
		for(String c: ncd.getNode(ID).getConnections()) {
			Gdx.app.log("traverse", "@"+ID);
			Gdx.app.log("traverse", "connection " + ncd.getConnection(c));
			Gdx.app.log("traverse", "destination " + ncd.getConnection(c).getDestination(ID));
			Gdx.app.log("traverse", "");
			if(!visited.contains(ncd.getConnection(c).getDestination(ID)))
			traverse(ncd,ncd.getConnection(c).getDestination(ID));
		}
	}
}


