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
 * When you tap the screen, bubbles appear on the screen. They expand and
 * eventually pop.
 */

public class GameActivity extends Activity implements Callback {
	/** Called when the activity is first created. */
	private SurfaceView surface;
	private SurfaceHolder holder;
	private final GameModel model = new GameModel();
	private final GameController controller = new GameController(this,model);
	private GameLoop gameLoop;
	private Paint backgroundPaint;
	private Paint diskPaint, squarePaint, targetPaint;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.out.println("staring the test method\n");

		model.addSquare(150f, 50f, 50f);
		model.addTarget(180f, 150f, 50f);
		Disk d = model.addDisk(150f, 500f, 50f);
		d.vx = 10;
		d.vy = 102;

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

		surface.setOnTouchListener(controller);

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

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		controller.setSize(width,height);
		// model.setSize(width, height);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		gameLoop = new GameLoop();
		gameLoop.start();
	}

	private void draw() {
		// TODO thread safety - the SurfaceView could go away while we are
		// drawing

		Canvas c = null;
		try {
			// NOTE: in the LunarLander they don't have any synchronization
			// here,
			// so I guess this is OK. It will return null if the holder is not
			// ready
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
		controller.setSize(width,height);
		
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

	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			// model.setSize(0,0);
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
					 * model.updateBubbles();
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

	enum State {
		WAIT, TOUCH_DISK, DRAG_DISK, FLING_DISK, ROUND_OVER, TOUCH_SQUARE;
	}

	class GameController implements OnTouchListener {

		private State currState;
		private float firstX,firstY;
		private Activity gameActivity;
		private GameModel gameModel;
		private Disk currDisk;
		private Square currSquare;
		private int width,height;
		
		

		public GameController(Activity gameActivity, GameModel gameModel) {
			this.gameActivity = gameActivity;
			this.gameModel = gameModel;
			currState = State.WAIT;
			currDisk=null;
			currSquare=null;
		}

		public void setSize(int w, int h){
			width=w; height = h;
		}
		
		/**
		 * This measures the arc of the finger swipe and uses that to throw the
		 * current disk ...
		 */
		public boolean onTouch(View v, MotionEvent event) {
			Disk d; Square s;float x,y;
			
			// get x,y coordinates from view and translate
			// to model coordinate system (with y=0 on bottom)
			
			x = event.getX();
			y = height - event.getY();
			
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				firstX = x;
				firstY = y; 
				if (((d=gameModel.touchingDisk(x,y))!= null) && (!d.isStatic) && (d.inside(x, y))){
					currDisk=d;
					d.vx=d.vy=0;
					currState = State.TOUCH_DISK;
				}else if  ((s=gameModel.touchingSquare(x,y))!= null){
					currSquare = s;
					currState = State.TOUCH_SQUARE;
				}
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (currState == State.TOUCH_DISK) {
					currDisk.move(firstX,firstY);
				} else if (currState == State.TOUCH_SQUARE) {
					currSquare.move(x,y);
				} 
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (currState == State.TOUCH_DISK){
					currState = State.WAIT;
					float dx = x-firstX;
					float dy = y-firstY;
					currDisk.vx=dx;
					currDisk.vy=dy;
					currDisk = null;
					return true;
				} else if (currState == State.TOUCH_SQUARE){
					currState = State.WAIT;
					currSquare = null;
					return true;
				}

			}
			return false;
		}
	}

}
