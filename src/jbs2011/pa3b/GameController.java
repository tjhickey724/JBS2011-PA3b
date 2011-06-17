package jbs2011.pa3b;

import jbs2011.pa3.Disk;
import jbs2011.pa3.GameModel;
import jbs2011.pa3.Square;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import android.util.Log;

/**
 * This takes input from the user and uses it to make corresponding changes to the model
 * and/or the view.
 * @author tim
 *
 */
public class GameController implements OnTouchListener {

	private State currState;
	private float firstX, firstY;
	private Activity gameActivity;
	private GameModel gameModel;
	private Disk currDisk;
	private Square currSquare;
	private int width, height;
	private final static String TAG="GC";

	public GameController(Activity gameActivity, GameModel gameModel) {
		this.gameActivity = gameActivity;
		this.gameModel = gameModel;
		currState = State.WAIT;
		currDisk = null;
		currSquare = null;
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;
	}

	/**
	 * This handles all events in which the user touches the screen.
	 * For example, the user can touch a square and drag it to a new position,
	 * or the user can touch a disk and flick it toward the target.
	 */
	public boolean onTouch(View v, MotionEvent event) {

		float x, y;

		// get x,y coordinates from view and translate
		// to model coordinate system (with y=0 on bottom)

		x = event.getX();
		y = height - event.getY();
        Log.d(TAG,"x="+x+"  y="+y+"h-y="+(height-y)+" currState="+currState+" firstX="+firstX+" firstY="+firstY);

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Disk d;
			Square s;
			firstX = x;
			firstY = y;
			d = gameModel.touchingDisk(x, y);
			s = gameModel.touchingSquare(x, y);
			Log.d(TAG,"d="+d);
			Log.d(TAG,"s="+s);
			
			if ((d != null) && (!d.isStatic)) {
				currDisk = d;
				d.vx = d.vy = 0;
				currState = State.TOUCH_DISK;
				Log.d(TAG,"TOUCH_DISK"+d);
			} else if (s!= null) {
				currSquare = s;
				currState = State.TOUCH_SQUARE;
				Log.d(TAG,"TOUCH_SQUARE"+s);
			}
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (currState == State.TOUCH_DISK) {
				currDisk.move(firstX, firstY);
			} else if (currState == State.TOUCH_SQUARE) {
				currSquare.move(x, y);
			}
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (currState == State.TOUCH_DISK) {
				currState = State.WAIT;
				float dx = x - firstX;
				float dy = y - firstY;
				currDisk.vx = dx;
				currDisk.vy = dy;
				currDisk = null;
				return true;
			} else if (currState == State.TOUCH_SQUARE) {
				currState = State.WAIT;
				currSquare = null;
				return true;
			}

		}
		return false;
	}
	
	public void levelOver(){
		// this is called by the model when the user wins the game!
	}
}
