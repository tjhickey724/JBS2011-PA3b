package jbs2011.pa3b;

import java.util.concurrent.TimeUnit;
import jbs2011.pa3.GameModel;
import android.util.Log;


/*
 * The Game loop simply draws the screen and updates the game continuously ..
 * It polls a variable running though and will exit the loop if running is set to false.
 * This class is the only one that ever draws on the Surface!
 * The Game loop also calls a method in the controller to convert model coordinates to
 * view coordinates for the model objects (disk, square).
 */
public class GameLoop extends Thread {
	private volatile boolean running = true;
	private static final String TAG="GL";
	GameView view;
	GameModel model;
	GameController controller;
/**
 * This defines the main loop of the game.
 * It runs a simple loop where it draws the view,
 * then updates the model until the model states the
 * level is over, at which point it informs the controller
 * which will reset the model and start over...
 * @param view
 * @param model
 * @param controller
 */
	public GameLoop(GameView view, GameModel model, GameController controller){
		this.model = model; this.controller= controller;
		this.view = view;
		Log.d(TAG,"starting another GameLoop");
	}
	
	/**
	 * the main draw/update until done loop
	 */
	public void run() {
		while (running) {
			try {
				TimeUnit.MILLISECONDS.sleep(10);
				synchronized(view){
				view.draw();
				}
				if (!model.levelOver)
					synchronized(model){
				     model.updateGame(System.currentTimeMillis());
					}
				else controller.levelOver();
					
				/*
				 * model.updateBubbles();
				 */

			} catch (InterruptedException ie) {
				running = false;
			}
		}
	}

	/**
	 * if we need to exit the loop gracefully, this method
	 * allows one to stop the loop without killing the 
	 * thread directly.
	 */
	public void safeStop() {
		running = false;
		interrupt();
	}
}
