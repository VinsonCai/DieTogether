package com.vinson.dietogether;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

	private DrawingThread mDrawingThread;
	private Game mGame;

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GameView(Context context) {
		super(context);
		init();
	}

	private void init() {
		getHolder().addCallback(new Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				stopDrawingThread();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {

			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				mGame.setScreenSize(width, height);
			}
		});

		mGame = new Game();
	}

	public void stopGame() {
		stopDrawingThread();
	}

	private void stopDrawingThread() {
		if (null != mDrawingThread) {
			mDrawingThread.stopThread();
			mDrawingThread = null;
		}
	}

	public void startGame() {
		if (null == mDrawingThread) {
			mGame.startGame();
			startDrawingThread();
		}
	}

	private void startDrawingThread() {
		mDrawingThread = new DrawingThread();
		mDrawingThread.start();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGame.onTouch(event);
		return true;
	}

	private class DrawingThread extends Thread {

		private boolean mIsRunning = false;
		private Thread mCurrentThread = null;

		@Override
		public void run() {
			super.run();

			mIsRunning = true;
			mCurrentThread = Thread.currentThread();

			while (mIsRunning) {
				Canvas canvas = getHolder().lockCanvas();
				mGame.drawGame(canvas);
				getHolder().unlockCanvasAndPost(canvas);

			}

		}

		public void stopThread() {
			mIsRunning = false;
			if (null != mCurrentThread) {
				mCurrentThread.interrupt();
				mCurrentThread = null;
			}
		}
	}
}
