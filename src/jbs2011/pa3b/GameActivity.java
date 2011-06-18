package jbs2011.pa3b;


import jbs2011.pa3.GameModel;

import android.app.Activity;

import android.os.Bundle;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

public class GameActivity extends Activity {
	/** Called when the activity is first created. */
	private SurfaceView surface;
	private SurfaceHolder holder;
	private GameModel model;
	private GameView view;
	private GameController controller;

	private static final String TAG="GA";

	/**
	 * When the activity starts we create a model, view, and controller for the game.
	 * The model and controller are separate classes
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setup a model for the game
		 model = new GameModel();

		 
        //  create the view which contqins a game_surface on which the game will be drawn
		setContentView(R.layout.game);
		// the drawing surface is created in the xml, get the surface from its ID
		surface = (SurfaceView) findViewById(R.id.game_surface);
		holder = surface.getHolder();


		// Next, create the GameController, pass it the model
		// and set it up to listen for game inputs sent to the Surface
		controller  = new GameController(this, model);
		surface.setOnTouchListener(controller);
		
		// Next, create the GameView which will draw on the Surface (as a back buffer!)
		// The view also handles the creation, resize, and destroy events for the surface
		view = new GameView(controller, holder, model);
		surface.getHolder().addCallback(view);
		

		Log.d(TAG,"surface created");
		//model.createLevel(2);



	}
	
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	}


	@Override
	protected void onPause() {
		// model.onPause(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// model.onResume(this);
	}

	



}
