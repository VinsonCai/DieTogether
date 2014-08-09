package com.vinson.dietogether;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private GameView mGameView;
	private Button mStartButton;
	private TextView mScoreTextView;

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

			}
		});

		mGameView = (GameView) findViewById(R.id.game_view);

		mScoreTextView = (TextView) findViewById(R.id.scrore_textView);
		mScoreTextView.setText(getString(R.string.score, 0));
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.exit(0);
	}
}
