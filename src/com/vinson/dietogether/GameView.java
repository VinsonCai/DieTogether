package com.vinson.dietogether;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;

import com.vinson.dietogether.events.FPSEvent;

import de.greenrobot.event.EventBus;

public class GameView extends TextureView {

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
		mGame = new Game();
		setSurfaceTextureListener(new SurfaceTextureListener() {

			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture texture) {

			}

			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
				mGame.setScreenSize(width, height);
			}

			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
				stopDrawingThread();
				return true;
			}

			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture texture, int arg1, int arg2) {

			}
		});
	}

//
//	private void init() {
//		getHolder().addCallback(new Callback() {
//
//			@Override
//			public void surfaceDestroyed(SurfaceHolder holder) {
//				stopDrawingThread();
//			}
//
//			@Override
//			public void surfaceCreated(SurfaceHolder holder) {
//
//			}
//
//			@Override
//			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//				mGame.setScreenSize(width, height);
//			}
//		});
//
//		mGame = new Game();
//	}

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
			int fpsCount = 0;
			mIsRunning = true;
			mCurrentThread = Thread.currentThread();
			FPSEvent event = new FPSEvent();
//			SurfaceHolder holder = getHolder();
			long start = System.currentTimeMillis();
			while (mIsRunning) {
				++fpsCount;
				long beforeLock = System.currentTimeMillis();
				Canvas canvas = lockCanvas();
				mGame.drawGame(canvas);
				unlockCanvasAndPost(canvas);
//				long sleepFor = System.currentTimeMillis() - beforeLock;
//				sleepFor = 16 - sleepFor;
//				if (sleepFor > 0) {
//					try {
//						Thread.sleep(sleepFor);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
				long end = System.currentTimeMillis();
				if (end - start > 1000) {
					start = end;
					event.mFps = fpsCount;
					fpsCount = 0;
					EventBus.getDefault().post(event);
				}
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
