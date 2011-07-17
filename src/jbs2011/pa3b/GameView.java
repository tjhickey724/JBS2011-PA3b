package jbs2011.pa3b;

import jbs2011.pa3.GameModel;
import jbs2011.pa3.Disk;
import jbs2011.pa3.Rectangle;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.graphics.Bitmap;
import android.util.Log;



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
	private Paint diskPaint, squarePaint, targetPaint,textPaint;
	private Bitmap background,backgroundD,player,playerD;
	private GameActivity gameActivity;
	
	public GameView(GameActivity context, GameController controller,SurfaceHolder holder,GameModel model){
		this.controller=controller;
		this.holder = holder;
		this.model=model;
		this.gameActivity=context;
        createPaints();
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.castle);
        player = BitmapFactory.decodeResource(context.getResources(), R.drawable.wine);

    	
        

	}


	/**
	 * When the drawing surface size changes we need to tell the controller so it
	 * can adjust the mapping between the view and the model
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		controller.setSize(width, height);
//		model.createLevel(1,width,height);

	}

		
	/**
	 *  When the drawing surface is created we start up a game loop,
	 *  the game loop just draws the scene and updates the model in an infinite loop
	 *  running in a separate thread. We create the thread and start it up here ...
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		if (gameLoop == null) {
		gameLoop = new GameLoop(gameActivity,this,model,controller);
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

		// draw the background (tiled)
		c.drawRect(0, 0, width, height, backgroundPaint);	
		
		if (model.levelOver){
			c.drawText("GAME OVER! score:"+model.score+" Best score:"+model.bestscore,0,50,textPaint);
		}else{
			c.drawText("  SCORE: "+ model.score+" || TIME LEFT: "+ model.timeRemaining ,0,50,textPaint);

		}
		
		if (model.levelOver) return;
      // backgroundD = background.createScaledBitmap(background, (int)width, (int)height, true);

	//	c.drawBitmap(backgroundD, model.backgroundOffset, 0, null);
	//	c.drawBitmap(backgroundD , model.backgroundOffset+model.backgroundWidth, 0, null);
		//c.drawBitmap(backgroundD , model.backgroundOffset2, height/4f, null);
		//c.drawBitmap(backgroundD , model.backgroundOffset2+model.backgroundWidth, height/4f, null);

		//draw the disks
		for (Disk d1 : model.disks) {
			Disk d=controller.modelToView(d1);
			// this is an ugly hack to see the circle (determining collisions)
			// and the image - a wine glass
			c.drawCircle(d.x, d.y, d.r, diskPaint);
	       // playerD = player.createScaledBitmap(player, (int)d.r, (int)(2*d.r), true);
		//	c.drawBitmap(playerD, d.x-d.r/2, d.y-d.r, null);
		}
		
		// draw the squares 
		for (int i=0; i<model.squares.size(); i++) {
			Rectangle d1 = model.squares.get(i);
			Rectangle d = controller.modelToView(d1);
			c.drawRect(d.x - d.w / 2, (d.y - d.h / 2),d.x +d.w/2,
					(d.y + d.h / 2), squarePaint);
		}
		for (int i=0;i<model.targets.size(); i++){
			Rectangle d1 = model.targets.get(i);
			Rectangle d=controller.modelToView(d1);
			c.drawRect(d.x - d.w / 2, (d.y - d.h / 2),d.x+d.w/2,
					d.y + d.h / 2, targetPaint); 
		}
		

		// play sound effects
		if (model.diskWallCollision)
		{
			Log.d("GV","dwc=true");
			//SoundManager.playSound(1); // find a better sound!
			model.diskWallCollision=false;
		}
		if (model.diskTargetCollision){
			SoundManager.playSound(2);
			model.diskTargetCollision=false;
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
		squarePaint.setColor(Color.YELLOW);
		squarePaint.setAntiAlias(true);

		targetPaint = new Paint();
		targetPaint.setColor(Color.RED);
		targetPaint.setAntiAlias(true);
		
		textPaint = new Paint();
		textPaint.setColor(Color.GREEN);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(40);
		
	}

	
}
