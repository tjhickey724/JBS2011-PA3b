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
	
	private int gameViewId, surfaceViewId;

	private static final String TAG="MGE";
	
	public GameActivity(GameModel theMod, GameController theCont, GameView theView) {
		Log.d(TAG,"GameActivity constructor");
		model = theMod;
		controller = theCont;
		view = theView;
	}
	
	public GameModel getModel() {
		return model;
	}
	
	public GameController getController() {
		return controller;
	}

	public void setKeyResources(int aGameView, int aSurfaceView) {
		this.gameViewId = aGameView;
		this.surfaceViewId = aSurfaceView;
	}
	
	/**
	 * When the activity starts we create a model, view, and controller for the game.
	 * The model and controller are separate classes
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(gameViewId);
		surface = (SurfaceView) findViewById(surfaceViewId);
		holder = surface.getHolder();
		surface.getHolder().addCallback(view);
		Log.d(TAG,"surface created! ");
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
