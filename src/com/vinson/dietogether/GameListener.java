package com.vinson.dietogether;

public interface GameListener {

	void onGameStarted();

	void onGameOver();

	void onGameScoreUpdate(int score);
}
