package com.vinson.dietogether;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vinson.dietogether.events.GameEvent;
import com.vinson.dietogether.events.GameScoreUpdateEvent;

import de.greenrobot.event.EventBus;

public class MainActivity extends Activity {

	private GameView mGameView;
	private Button mStartButton;
	private TextView mScoreTextView;

	private boolean mGameStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mStartButton = (Button) findViewById(R.id.start_game_button);
		mStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mGameStarted) {
					mGameView.stopGame();
					mGameStarted = false;
					mStartButton.setText("Start");
				} else {
					mGameView.startGame();
					mGameStarted = true;
					mStartButton.setText("Stop");
				}
			}
		});

		mGameView = (GameView) findViewById(R.id.game_view);

		mScoreTextView = (TextView) findViewById(R.id.scrore_textView);
		mScoreTextView.setText(getString(R.string.score, 0));
	}

	private void onGameOver() {
		mGameView.stopGame();
		Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
		mGameStarted = false;
		mStartButton.setText("Start");
	}

	public void onEventMainThread(GameEvent event) {
		onGameOver();
	}

	public void onEventMainThread(GameScoreUpdateEvent event) {
		mScoreTextView.setText(getString(R.string.score, event.mScore));
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
		System.exit(0);
	}
}
