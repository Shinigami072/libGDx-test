package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AI.Controller;
import com.mygdx.game.AI.state.StateController;
import com.mygdx.game.node.Connection;
import com.mygdx.game.node.ConsoleOutputActor;
import com.mygdx.game.node.Node;
import com.mygdx.game.node.NodeConnectionDictionary;

/**
 * Created by shinigami on 25.05.17.
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {
    private Game game;
    private Stage stage;
    private ParallaxBackground parallaxBackground;
    private Controller AI,AI2;
    Vector2 direction;
    NodeConnectionDictionary ncd;
    Owner player = new Owner("player", Color.CYAN);
    public GameScreen(Game msGame) {

        game = msGame;
        stage = new Stage(new ScreenViewport());
    Gdx.app.log("SCREEN","GameScreen");
        Array<Texture> textures = new Array<Texture>();
        for(int i = 1; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("parallax3/"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        parallaxBackground  = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        parallaxBackground.layerDiff=2f;
        //stage.addActor(parallaxBackground);
        MyGdxGame.console = new ConsoleOutputActor();
        MyGdxGame.console.setPosition(10f,Gdx.graphics.getHeight()-10f);
        stage.addActor(MyGdxGame.console);

        direction= new Vector2();
         ncd = new NodeConnectionDictionary(player);
//		new Node("alfa-1",ncd).setPosition(160f,0f);
//		new Node("alfa-2",ncd, Node.Type.L_2).setPosition(-50f,50f);
//		new Node("beta-1",ncd, Node.Type.L_1).setPosition(400f,-300f);
//        new Node("gamma-1",ncd).setPosition(800f,-150f);
//        new Node("delta-1",ncd).setPosition(1000f,-300f);
//        new Node("delta-2",ncd, Node.Type.Exit,"CPU").setPosition(1000f,300f);
//
//
//        new Connection("r-a1","root","alfa-1",ncd).setMode(Connection.mode.UNIDIRECTIONAL);
//		new Connection("r-a2","root","alfa-2",ncd);
//		new Connection("a1-b1","alfa-1","beta-1",ncd).setMode(Connection.mode.UNIDIRECTIONAL);
//        new Connection("b1-g1","beta-1","gamma-1",ncd);
//        new Connection("g1-d1","gamma-1","delta-1",ncd);
//        new Connection("g1-d2","gamma-1","delta-2",ncd);
        Utility.generateNodeTree(ncd,4,5);
        ncd.setPosition(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f);
        stage.addActor(ncd);
        Node n = ncd.lastNode();
        n.setOwner("CPU");
        n.type = Node.Type.Exit;
        AI = new StateController(ncd,n,ncd.getOwner("CPU"));
        n =ncd.randomNode();
        n.setOwner("player");
        n.type = Node.Type.L_1;

        AI2 = new StateController(ncd,n,ncd.getOwner("player"));
        ncd.getNode("root").setOwner(null);
    }

    @Override
    public void show() {
        InputMultiplexer mul = new InputMultiplexer();
        mul.addProcessor(stage);
        mul.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(mul);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        direction.set(MathUtils.random(-1f,1f),MathUtils.random(-1f,1f));
        parallaxBackground.setSpeed(        parallaxBackground.speed.add(direction));
        AI.act(delta);
        AI2.act(delta);
        stage.act();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        ncd.moveBy(deltaX,-deltaY);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
