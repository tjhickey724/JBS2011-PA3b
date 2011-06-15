package jbs2011.tjhickey.pa3;

import android.app.Activity;
import android.os.Bundle;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import jbs2011.pa3.tjhickey.GameModel;
import jbs2011.pa3.tjhickey.Disk;
import jbs2011.pa3.tjhickey.Square;


 


import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnTouchListener;

/**
 * When you tap the screen, bubbles appear on the screen. They expand and eventually pop.
 */

public class GameActivity extends Activity  implements Callback, OnTouchListener {
    /** Called when the activity is first created. */
	private SurfaceView surface;
	private SurfaceHolder holder;
	private final GameModel model = new GameModel();
	private GameLoop gameLoop;
	private Paint backgroundPaint;
	private Paint diskPaint,squarePaint,targetPaint;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
		
		System.out.println("staring the test method\n");

		model.addSquare(100f,100f,200f);
		model.addTarget(100f,300f,200f);
		Disk d = model.addDisk(150f,500f,50f);
		d.vx=1; d.vy=20;
    	
    	setContentView(R.layout.game);
    	
    	surface = (SurfaceView) findViewById(R.id.game_surface);
    	holder = surface.getHolder();
    	surface.getHolder().addCallback(this);
    	
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
		
		surface.setOnTouchListener(this);

    }
    
	@Override
	protected void onPause() {
//		model.onPause(this);		
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		model.onResume(this);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		//model.setSize(width, height);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		gameLoop = new GameLoop();
		gameLoop.start();
	}
	
	private void draw() {
		// TODO thread safety - the SurfaceView could go away while we are drawing
		
		Canvas c = null;
		try {
			// NOTE: in the LunarLander they don't have any synchronization here,
			// so I guess this is OK. It will return null if the holder is not ready
			c = holder.lockCanvas();
			
			// TODO this needs to synchronize on something
			if (c != null) {
				doDraw(c);
			}
		} finally {
			if (c != null) {
				holder.unlockCanvasAndPost(c);
			}
		}
	}
	
	private void doDraw(Canvas c) {
		int width = c.getWidth();
		int height = c.getHeight();
		c.drawRect(0, 0, width, height, backgroundPaint);
		
		for(Disk d:model.disks){
			c.drawCircle(d.x,d.y,d.r,diskPaint);
		}
		for(Square d:model.squares){
			//c.drawRect(d.x-d.w/2,d.y-d.w/2,d.x+d.w/2,d.y+d.w/2,squarePaint);
		}
		for(Square d:model.targets){
			//c.drawRect(d.x-d.w/2,d.y-d.w/2,d.x+d.w/2,d.y+d.w/2,targetPaint);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
		//	model.setSize(0,0);
			gameLoop.safeStop();
		} finally {
			gameLoop = null;
		}
	}
    
	private class GameLoop extends Thread {
		private volatile boolean running = true;
		
		public void run() {
			while (running) {
				try {
					// TODO don't like this hardcoding
					TimeUnit.MILLISECONDS.sleep(5);
					
					draw();
					model.updateGame(System.currentTimeMillis());
					/*
					model.updateBubbles();
					*/

				} catch (InterruptedException ie) {
					running = false;
				}
			}
		}
		
		public void safeStop() {
			running = false;
			interrupt();
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			/*
			model.addBubble(event.getX(), event.getY());
			*/
			return true;
		}
		return false;
	}


}
