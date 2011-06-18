package jbs2011.pa3b;

import jbs2011.pa3.GameModel;
import jbs2011.pa3.Disk;
import jbs2011.pa3.Square;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;


/**
 * This class implements the view of the game. Its main purpose is to know
 * how to draw a representation of the model onto the Surface when called to do so.
 * It also responds to the events generated with the surface is created, changed, destroyed.
 * It reads from the model but does not change anything in the model. The view also starts
 * up the game loop when the surface is created ... 
 * @author tim
 *
 */
public class GameView  implements Callback{
	
	private GameController controller;
	private SurfaceHolder holder;
	private GameModel model;
	private GameLoop gameLoop;
	private Paint backgroundPaint;
	private Paint diskPaint, squarePaint, targetPaint;
	
	public GameView(GameController controller,SurfaceHolder holder,GameModel model){
		this.controller=controller;
		this.holder = holder;
		this.model=model;
        createPaints();
        

	}


	/**
	 * When the drawing surface size changes we need to tell the controller so it
	 * can adjust the mapping between the view and the model
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		controller.setSize(width, height);

	}

		
	/**
	 *  When the drawing surface is created we start up a game loop,
	 *  the game loop just draws the scene and updates the model in an infinite loop
	 *  running in a separate thread. We create the thread and start it up here ...
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		if (gameLoop == null) {
		gameLoop = new GameLoop(this,model,controller);
		gameLoop.start();
		}

	}

	/**
	 * This uses the surface holder to get a lock on the canvas,
	 * the draws a representation of the model on the canvas (if its not null),
	 * and finally releases the lock and posts a "draw" event to the main GUI
	 * which will copy the canvas onto the screen.
	 */
	public void draw() {

		Canvas c = null;
		try {
			c = holder.lockCanvas();

			if (c != null) {
				doDraw(c);
			
			}
		} finally {
			if (c != null) {
				holder.unlockCanvasAndPost(c);
			}
		}
	}

	/*
	 * The drawing method simply draws the disks, squares, and targets
	 * after it paints the entire screen with the background color
	 */
	private void doDraw(Canvas c) {
		int width = c.getWidth();
		int height = c.getHeight();
		controller.setSize(width, height);

		//Log.d("GA","w="+width+" h="+height);

		c.drawRect(0, 0, width, height, backgroundPaint);

		for (Disk d : model.disks) {
			c.drawCircle(d.x, height - d.y, d.r, diskPaint);
		}
		for (Square d : model.squares) {
			c.drawRect(d.x - d.w / 2, height - (d.y + d.w / 2), d.x + d.w / 2,
					height - (d.y - d.w / 2), squarePaint);
		}
		for (Square d : model.targets) {
			c.drawRect(d.x - d.w / 2, height - (d.y + d.w / 2), d.x + d.w / 2,
					height - (d.y - d.w / 2), targetPaint);
		}
	}

	/**
	 * When the surface is destroyed we stop the game loop
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			gameLoop.safeStop();
		} finally {
			gameLoop = null;
		}
	}


	/*
	 * The disk, square, and targets all have different colors
	 */
	private void createPaints(){
		backgroundPaint = new Paint();
		backgroundPaint.setColor(Color.BLUE);

		diskPaint = new Paint();
		diskPaint.setColor(Color.BLACK);
		diskPaint.setAntiAlias(true);

		squarePaint = new Paint();
		squarePaint.setColor(Color.WHITE);
		squarePaint.setAntiAlias(true);

		targetPaint = new Paint();
		targetPaint.setColor(Color.RED);
		targetPaint.setAntiAlias(true);
	}

	
}
