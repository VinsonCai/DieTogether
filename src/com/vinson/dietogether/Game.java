package com.vinson.dietogether;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.vinson.dietogether.model.Block;
import com.vinson.dietogether.model.Block.BlockListener;
import com.vinson.dietogether.model.RunningMan;

public class Game {

	private static final String TAG = "Game";
	private GameListener mGameListener;
	private RunningMan mRunningMan;
	private ArrayList<Block> mBlocks;

	private int mWidth;
	private int mHeight;

	public Game() {
		mBlocks = new ArrayList<Block>();
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
		mBlocks.clear();
	}

	private void generateBlock() {
		Block newBlock = new Block(mWidth, 100);
		newBlock.setBlockListener(new BlockListener() {

			@Override
			public void onOutOfScreen(Block block) {
				mBlocks.remove(block);
			}
		});
		mBlocks.add(newBlock);
	}

	public void drawGame(Canvas canvas) {
		drawBackground(canvas);
		mRunningMan.drawSelf(canvas);

		for (Block block : mBlocks) {
			block.drawSelf(canvas);
		}

		if (mBlocks.size() < 1) {
			generateBlock();
		}

		checkConflick();
	}

	private void checkConflick() {
		for (Block block : mBlocks) {
			if (mRunningMan.isConflickWith(block)) {
				fireOnGameOver();
			}
		}
	}

	public void drawBackground(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
	}

	public void setGameListener(GameListener listener) {
		mGameListener = listener;
	}

	private void fireOnGameOver() {
		if (null != mGameListener) {
			mGameListener.onGameOver();
		}
	}

}
