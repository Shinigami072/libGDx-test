package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.node.ConsoleOutputActor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter{
	//SpriteBatch batch;
	//TextureAtlas textureAtlas;
    Stage stage;
	Skin skin;
	Label labelDetails;
	Label labelMessage;
	TextButton button;
	TextArea textIPAddress;
	TextArea textMessage;
	SpriteBatch batch;
//	TextureAtlas textureAtlas1;

	OrthographicCamera camera;

	public static TextureRegion t;

//	Sprite sprite;
//	Animation<TextureRegion> anim;
//	Animation<TextureRegion> ecusplosion;

	float elapsedTime =0f;
	public final static float VIRTUAL_SCREEN_HEIGHT = 960;
	public final static float VIRTUAL_SCREEN_WIDTH = 540;
	public static ConsoleOutputActor console;

	@Override
	public void create () {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage = new Stage();
		Gdx.app.log("CREATE","before for");
		List<String> addresses = new ArrayList<String>();
		try{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			for(NetworkInterface ni: Collections.list(interfaces))
				for(InetAddress address: Collections.list(ni.getInetAddresses()))
					if(address instanceof Inet4Address)
						addresses.add(address.getHostAddress());

		}catch(SocketException e){
			e.printStackTrace();
		}
		Gdx.app.log("CREATE","after for");

		String ipAddresses ="";
		for(String address:addresses)
			if(!address.equals("127.0.0.1"))
			ipAddresses+=address+"\n";
		Gdx.app.log("CREATE",ipAddresses);

		VerticalGroup vg = new VerticalGroup().space(3f).pad(1f).fill();
		vg.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());//VIRTUAL_SCREEN_WIDTH, VIRTUAL_SCREEN_HEIGHT);
		Gdx.app.log("CREATE","after vg");
		labelDetails = new Label(ipAddresses,skin);
		labelMessage = new Label("msG",skin);
		button = new TextButton("Send",skin);
		textIPAddress = new TextArea("",skin);
		textMessage = new TextArea("",skin);

		// Add them to scene
		vg.addActor(labelDetails);
		vg.addActor(labelMessage);
		vg.addActor(textIPAddress);
		vg.addActor(textMessage);
		vg.addActor(button);

		// Add scene to stage
		stage.addActor(vg);
		Gdx.app.log("CREATE","after vg");

		// Setup a viewport to map screen to a 480x640 virtual resolution
		// As otherwise this is way too tiny on my 1080p android phone.


		new Thread( new Runnable(){
			public void run(){
				ServerSocketHints server = new ServerSocketHints();
				server.acceptTimeout =0;

				ServerSocket serverSocket= Gdx.net.newServerSocket(Net.Protocol.TCP,9021,server);

				while(true){
					Socket socket = serverSocket.accept(null);
					BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
					try{
						labelMessage.setText(labelMessage.getText()+"\n"+socket.getRemoteAddress()+":"+bufferedReader.readLine());
						Gdx.app.log("server","recieved");
						socket.getOutputStream().write("recieved\n".getBytes());
					}
					catch(IOException e){
						e.printStackTrace();
					}

				}

			}
		}).start();

		// Wire up a click listener to our button
		button.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){

				// When the button is clicked, get the message text or create a default string value
				String textToSend = new String();
				if(textMessage.getText().length() == 0)
					textToSend = "Doesn't say much but likes clicking buttons\n";
				else
					textToSend = textMessage.getText() + ("\n"); // Brute for a newline so readline gets a line

				SocketHints socketHints = new SocketHints();
				// Socket will time our in 4 seconds
				socketHints.connectTimeout = 4000;
				//create the socket and connect to the server entered in the text box ( x.x.x.x format ) on port 9021
				Socket socket = Gdx.net.newClientSocket(Net.Protocol.TCP, textIPAddress.getText(), 9021, socketHints);
				try {
					// write our entered message to the stream
					socket.getOutputStream().write(textToSend.getBytes());
					byte[] c = new byte[10];
					socket.getInputStream().read(c);
					String s = "";
					for(byte b:c)
					s+=(char)b;
					Gdx.app.log("cliant",""+s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		Gdx.input.setInputProcessor(stage);
//		textureAtlas = new TextureAtlas("spritesheet.atlas");
//		textureAtlas1 = new TextureAtlas("Spritesheet/shipsMiscellaneous_sheet.atlas");
//		for(TextureAtlas.AtlasRegion r:textureAtlas1.getRegions())
//		System.out.println(r);
//		TextureAtlas.AtlasRegion region = textureAtlas.findRegion("planeBlue1");
//		Gdx.input.setInputProcessor(new GestureDetector(this));
//		Timer.schedule(new Timer.Task(){
//			@Override
//			public void run(){
//				currentFrame++;
//				if(currentFrame > 5)
//					currentFrame =1;
//				currentAtlasKey = "plane"+planeColor+(currentFrame>3?3-(currentFrame-3):currentFrame);
//				sprite.setRegion(textureAtlas.findRegion(currentAtlasKey));
//			};
//
//		},0,1/30.0f);
//		anim = new Animation(1/15f, textureAtlas.getRegions());
//		ecusplosion =  new Animation(1/5f,
//				textureAtlas1.findRegion("explosion1"),
//				textureAtlas1.findRegion("explosion2"),
//				textureAtlas1.findRegion("explosion3"));
//		ecusplosion.setPlayMode(Animation.PlayMode.NORMAL);
	}

	@Override
	public void render () {
		//Gdx.app.log("Render","start");
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		elapsedTime += Gdx.graphics.getDeltaTime();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		stage.draw();
		batch.end();
	///	Gdx.app.log("Render","end");
	}
	
	@Override
	public void dispose () {
		stage.dispose();
	}
}
