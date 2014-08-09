package com.vinson.dietogether;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private GameView mGameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGameView = new GameView(MainActivity.this);
		setContentView(mGameView);

	}

	@Override
	protected void onPause() {
		super.onPause();
		System.exit(0);
	}
}
