package jbs2011.pa3b;



import jbs2011.pa3.GameModel;

import android.app.Activity;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import android.util.Log;

/**
 * This game consists of the user flinging disks into the air trying to hit
 * a (red) target square without hitting static blocking element, initiallysquares. 
 * If it does hit a static object, it becomes static itself making it harder
 * to get to the target.  The user can grab a disk in flight and refling it
 * but cannot drag it around the screen. The level ends when the target is hit
 * and the game goes to the next level.
 * 
 * This game is a sketetal version of a simple game that was developed
 * as part of Programming Assignment 3 for the Brandeis University JBS
 * in Summer of 2011.
 */

public class GameActivity extends Activity implements OnClickListener{
	/** Called when the activity is first created. */
	private SurfaceView surface;
	private SurfaceHolder holder;
	private GameModel model;
	private GameView view;
	private GameController controller;
	private Handler handler;
	private Handler myHandler;
	private Button quitbutton;
	private TextView score;

	private static final String TAG="GA";

	/**
	 * When the activity starts we create a model, view, and controller for the game.
	 * The model and controller are separate classes
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		myHandler = new Handler(); // this is used to allow the score to be updated

		// setup a model for the game
		 model = new GameModel();

		 
        //  create the view which contqins a game_surface on which the game will be drawn
		setContentView(R.layout.game);
		// the drawing surface is created in the xml, get the surface from its ID
		surface = (SurfaceView) findViewById(R.id.game_surface);
		holder = surface.getHolder();


		// Next, create the GameController, pass it the model
		// and set it up to listen for game inputs sent to the Surface
		controller  = new GameController(handler,this,model);
		surface.setOnTouchListener(controller);
		
		// Next, create the GameView which will draw on the Surface (as a back buffer!)
		// The view also handles the creation, resize, and destroy events for the surface
		view = new GameView(this, controller, holder, model);
		surface.getHolder().addCallback(view);
		
		quitbutton = 	(Button) findViewById(R.id.quitbutton);
		quitbutton.setOnClickListener(this);
		

		//controller.startNewGame();
		

		Log.d(TAG,"surface created! ");
		//model.createLevel(2);
        SoundManager.initSounds(this);
        SoundManager.loadSounds();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Music.play(this, R.raw.piano);

	}
	
	public void onClick(View v){
    	if (v==quitbutton)
    		SoundManager.playSound(2);
    		controller.startNewGame();
    		//this.finish();
	}
	
	
	void goToLevel() {
		//startActivity(new Intent(this, Levels.class));
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}



	   
    @Override
    protected void onResume()
    {
     super.onResume();
     setVolumeControlStream(AudioManager.STREAM_MUSIC);
          Music.play(this, R.raw.piano);

    }
    
    @Override
    protected void onPause()
    {
     super.onPause();
     Music.stop(this);
    }
    
    @Override
    protected void onDestroy()
    {
     super.onDestroy();
     Music.stop(this);
    }




}
