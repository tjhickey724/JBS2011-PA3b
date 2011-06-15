package jbs2011.tjhickey.pa3;

import jbs2011.pa3.tjhickey.GameModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * This data model tracks bubbles on the screen.
 * 
 * @see BubblesActivity
 */
public class GameModelProxy implements OnCompletionListener {
	
	private GameModel model = new GameModel();
    private long lastTimeMs = -1;
	
	private final List<MediaPlayer> players = new LinkedList<MediaPlayer>();
	private boolean running = false;

	
	public final Object LOCK = new Object();
	
	public GameModelProxy() {		
	}
	
	public void onResume(Context context) {
		synchronized (LOCK) {
			for (int i=0; i<4; i++) {
				MediaPlayer mp = MediaPlayer.create(context, R.raw.pop);
				mp.setVolume(1f, 1f);
				players.add(mp);
				try {
					mp.setLooping(false);
					mp.setOnCompletionListener(this);
					
					// TODO: there is a serious bug here. After a few seconds of
					// inactivity, we see this in LogCat:
					//   AudioHardwareMSM72xx Going to standby 
					// then the sounds don't play until you click several more
					// times, then it starts working again
					
				} catch (Exception e) {
					e.printStackTrace();
					players.remove(mp);
				}
			}
			running = true;
		}
	}
	
	public void onPause(Context context) {
		synchronized (LOCK) {
			running = false;
			for (MediaPlayer p : players) {
				p.release();
			}
			players.clear();
		}
	}

    public void updateGame() {
        long curTime = System.currentTimeMillis();
        if (lastTimeMs < 0) {
            lastTimeMs = curTime;
            // this is the first reading, so don't change anything
            return;
        }
        long elapsedMs = curTime - lastTimeMs;
        lastTimeMs = curTime;
        
        model.updateGame(curTime);
        

        MediaPlayer mp = null;
 
    	synchronized (LOCK) {
   
    		

    		if (false) {

    			// since a bubble popped, try to get a media player
    			if (!players.isEmpty()) {    				
    				mp = players.remove(0);
    			}
    		}
    	}
    	
    	if (mp != null) {
    		//System.out.println("**pop**");
    		mp.start(); 
    	}
    }

	public void onCompletion(MediaPlayer mp) {
		synchronized (LOCK) {
			if (running) {
	    		mp.seekTo(0);
				//System.out.println("on completion!");
	    		// return the player to the pool of available instances
				players.add(mp);
			}
		}
	}
}
