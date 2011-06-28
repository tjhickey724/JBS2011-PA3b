package edu.brandeis.minigamee;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import android.util.Log;

/**
 * This package is derived from PA3a and PA3b from JBS 2011. We are turning a specific architecture
 * for a game into a tiny game engine. To use this engine, we need to define, at least three
 * classes. A GameModel, a GameView and a GameController, each implementing a specific set of 
 * interfaces.
 */


public class GameActivity extends Activity {

	/** Called when the activity is first created. */
	private SurfaceView surface;
	private SurfaceHolder holder;
	private GameModel model;
	private GameView view;
	private GameController controller;
	
	private int gameView, surfaceView;

	private static final String TAG="GA";
	
	public GameActivity(GameModel mod, GameController cont, GameView view) {
		model = mod;
		controller = cont;
	}
	
	public GameModel getModel() {
		return model;
	}
	
	public GameController getController() {
		return controller;
	}

	public void setKeyResources(int aGameView, int aSurfaceView) {
		this.gameView = aGameView;
		this.surfaceView = aSurfaceView;
	}
	
	/**
	 * When the activity starts we create a model, view, and controller for the game.
	 * The model and controller are separate classes
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		 
        //  create the view which contqins a game_surface on which the game will be drawn
//		setContentView(R.layout.game);
		setContentView(gameView);
		// the drawing surface is created in the xml, get the surface from its ID
//		surface = (SurfaceView) findViewById(R.id.game_surface);
		surface = (SurfaceView) findViewById(surfaceView);

		holder = surface.getHolder();
		surface.getHolder().addCallback(view);
		

		Log.d(TAG,"surface created! ");
		//model.createLevel(2);



	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
