package com.vinson.dietogether;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.vinson.dietogether.events.GameEvent;
import com.vinson.dietogether.events.GameScoreUpdateEvent;
import com.vinson.dietogether.model.Block;
import com.vinson.dietogether.model.Block.BlockListener;
import com.vinson.dietogether.model.RunningMan;

import de.greenrobot.event.EventBus;

public class Game {

	private static final String TAG = "Game";
	private static final int MIN_BLOCK_INTERVAL = 50;
	private static final int RANDOM_BLOCK_INTERVAL = 150;
	private RunningMan mRunningMan;

	private LinkedBlockingQueue<Block> mBlockingQueue = new LinkedBlockingQueue<Block>();

	private int mWidth;
	private int mHeight;
	private int mCount = 0;
	private Random mRandom = new Random();
	private int mScore = 0;

	public Game() {
		mRunningMan = new RunningMan(100, 100);

	}

	public void onTouch(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mRunningMan.jump();
			break;

		default:
			break;
		}
	}

	public void setScreenSize(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	public void startGame() {
		mBlockingQueue.clear();
		mScore = 0;
	}

	private void generateBlockIfNeed() {
		--mCount;
		if (mCount < 0) {
			generateBlock();
			mCount = MIN_BLOCK_INTERVAL + mRandom.nextInt(RANDOM_BLOCK_INTERVAL);
		}
	}

	private void generateBlock() {
		Block newBlock = new Block(1024, 100);
		newBlock.setBlockListener(new BlockListener() {

			@Override
			public void onOutOfScreen(Block block) {

				mBlockingQueue.remove(block);
			}
		});
		mBlockingQueue.add(newBlock);
	}

	public void drawGame(Canvas canvas) {
		drawBackground(canvas);
		mRunningMan.drawSelf(canvas);

		for (Block block : mBlockingQueue) {
			block.drawSelf(canvas);
		}

		checkConflick();

		generateBlockIfNeed();

		mScore += 2;
		fireOnGameUpdate();
	}

	private void checkConflick() {
		for (Block block : mBlockingQueue) {
			if (mRunningMan.isConflickWith(block)) {
				fireOnGameOver();
			}
		}
	}

	public void drawBackground(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
	}

	private void fireOnGameOver() {
		GameEvent event = new GameEvent();
		event.mEventType = GameEvent.TYPE_GAME_OVER;
		EventBus.getDefault().post(event);
	}

	private void fireOnGameUpdate() {
		GameScoreUpdateEvent event = new GameScoreUpdateEvent();
		event.mScore = mScore;
		EventBus.getDefault().post(event);
	}
}
