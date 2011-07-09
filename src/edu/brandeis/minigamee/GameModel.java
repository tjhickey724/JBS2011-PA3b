package edu.brandeis.minigamee;

import android.util.Log;

public abstract class GameModel {
	public static String TAG="MGE-GM";

	/**
	 * this is set to true when the level is completed
	 */
	public boolean levelOver=true;
	
	public GameModel() {
		Log.d(TAG, "Game Model Constructed");
	}
	
    public abstract void updateGame(long timeMillis);

    public abstract void resetGame();

	public abstract void createLevel(int level, int width, int height);
	
	// last time the doDraw method was called
	private long lastTime;
	// current time that the doDraw method is called
	private long currTime;
	// beginning time for the current game 
	private long startTime=0;
	
	/**
	 * length of a game in seconds
	 */
	public float gameLength=15; // seconds
	/**
	 * time remaining in the current game
	 */
	public float timeRemaining;
	/**
	 * number of games won so far
	 */
	public int wins;
	/**
	 * number of games lost so far
	 */
	public int losses;
	
	// time since the last call to doDraw
	private long dt;
	
	// true if this is the first time doDraw has been called since the application was started
	private boolean firstEval=true;
	
	/**
	 * this is set to true when the user wins 
	 */
	public boolean userWon = false;
	
	/**
	 * this is set to true when the user loses
	 */
	public boolean userLost = false;

}
