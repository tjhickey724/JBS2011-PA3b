package edu.brandeis.minigamee;

public abstract class GameModel {

	public boolean levelOver;
	
    public abstract void updateGame(long timeMillis);

    public abstract void resetGame();

	public abstract void createLevel(int level, int width, int height);

}
