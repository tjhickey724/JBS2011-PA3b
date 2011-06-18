package jbs2011.pa3b;

/**
 * This keeps track of the game state from the controllers point of view,
 * that is whether the controller is waiting for an event, or processing a
 * touch_disk action or a touch_square action, both of which consist of a
 * sequence of motion_events starting with a MOTION_DOWN, progressing through
 * a MOTION_MOVE, and ending with a MOTION_UP.  The main point here is that the
 * controller needs to keep track of what its state is...
 * 
 * A similar approach could be used for game state if the update_model operation
 * takes on different behaviors in different states... (e.g. during power ups ...)
 * @author tim
 *
 */
public enum State {
	WAIT, TOUCH_DISK, TOUCH_SQUARE;
}
