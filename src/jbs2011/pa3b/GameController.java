package jbs2011.pa3b;

import jbs2011.pa3.Disk;
import jbs2011.pa3.GameModel;
import jbs2011.pa3.Square;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import android.graphics.PointF;

import android.util.Log;

/**
 * This takes input from the user and uses it to make corresponding changes to the model
 * and/or the view. This also keeps track of the mapping from model to view and vice versa
 * ((It might be better to have that functionality in the GameView class, but I like all the
 * logic to be in the GameController.)) 
 * @author tim
 *
 */
public class GameController implements OnTouchListener {

	private State currState;
	private float firstX, firstY;
	private GameModel gameModel;
	private Disk currDisk;
	private Square currSquare;
	private int width, height;
	private float zoom=1.0f; // this doesn't seem to work correctly with zoom != 1f
	private final static String TAG="GC";

	/**
	 * This processes all user input to the game and uses that input to update the model.
	 * It allows the user to grab disks and squares and move them or flick them and so needs
	 * to keep track of the currently "selected" disk or square...
	 * @param gameModel
	 */
	public GameController(GameModel gameModel) {
		this.gameModel = gameModel;
		currState = State.WAIT;
		currDisk = null;
		currSquare = null;
	}

	/**
	 * Update the width and height of the playing area.
	 */
	public void setSize(int w, int h) {
		width = w;
		height = h;
		//Log.d(TAG,"w="+w+"  h="+h);

	}
	
	/**
	 * Convert from PointF p from View coordinats to Model Coordinates
	 * @param p
	 * @return q
	 */
	public PointF viewToModel(PointF p){
		PointF q = new PointF(p.x*zoom,(height-p.y)*zoom);
		return q;		
	}
	
	
	/**
	 * Convert PointF p from Model coordinats to View Coordinates
	 * @param p
	 * @return q
	 */
	public PointF modelToView(PointF p){
		PointF q = new PointF(p.x/zoom,height-p.y/zoom);
		return q;		
	}
	
	/**
	 * Convert disk d from model coordinates to view coordinates
	 * @param d
	 * @return q
	 */
	public Disk modelToView(Disk d){
		return new Disk(d.x/zoom,height-d.y/zoom,d.r/zoom);
	}
	
	/**
	 * convert square s from model coordinates to view coordinates
	 * @param s
	 * @return new square
	 */
	public Square modelToView(Square s){
		return new Square(s.x/zoom,height-s.y/zoom,s.w/zoom,s.isTarget);
	}
	
	

	/**
	 * This handles all events in which the user touches the screen.
	 * Currently, the user can touch a disk and drag in a direction, when the
	 * user lifts their finger the disk shoots off in that direction with a speed relative
	 * to the distance from the original position. The disk stays fixed while the finger drags
	 * (really we should have a rubber band object to visualize this). The user doesn't have to
	 * exactly touch the disk, it takes the closest disk to the touch within 100 pixels (or some other limit ...)
	 * 
	 * Also, the user can touch a square a drag it to another location.
	 * 
	 * All other touch events are ignored for now
	 * 
	 */
	public boolean onTouch(View v, MotionEvent event) {
        PointF p;
		float x, y;

		// get x,y coordinates from view and translate
		// to model coordinate system (with y=0 on bottom)
		synchronized(gameModel){
			// at this point we convert from raw coordinates to the model coordinates
			// raw coordinates have the origin (0,0) at the upper left corner, but the
			// model system has it at the lower left corner.
			// The View may need to use this transformation to draw on the screen.....
		p = viewToModel(new PointF(event.getX(),event.getY()));
		x = p.x;
		y = p.y;
        Log.d(TAG,"x="+x+"  y="+y+"h-y="+(height-y)+" currState="+currState+" firstX="+firstX+" firstY="+firstY);

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Disk d;
			Square s;
			d = gameModel.touchingDisk(x, y);
			s = gameModel.touchingSquare(x, y);
			Log.d(TAG,"d="+d);
			Log.d(TAG,"s="+s);
			
			if ((d != null) && (!d.isStatic)) {
				currDisk = d;
				currDisk.vx = currDisk.vy = 0;
				currDisk.weightless=true;
				currState = State.TOUCH_DISK;
				// record the position of the disk as we will use it when we let go of the disk, to fling it in ACTION_UP
				firstX = d.x;
				firstY = d.y;
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
				currDisk.weightless=false;
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
	}
	
	/**
	 * this is called from the GameLoop when it polls the model and detects that the game is over.
	 * It resets the game and creates a new level. Another possibility would be to go to a level completed activity....
	 * but we don't do that yet...
	 */
	public void levelOver(){
		// this is called by the model when the user wins the game!
		if (gameModel.levelOver){
			synchronized(gameModel){

			gameModel.resetGame();
			gameModel.createLevel(2);
			for(Square s:gameModel.targets){
				Log.d(TAG,"target = "+s);
			}
			gameModel.levelOver=false;
			}
			
		}
	}
}
