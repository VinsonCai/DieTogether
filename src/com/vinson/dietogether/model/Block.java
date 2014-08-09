package com.vinson.dietogether.model;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class Block extends AbstractShape {

	private static final int MOVE_INTERVAL = 2;
	private BlockListener mBlockListener;
	private Paint mPaint;
	private int mHeight;
	private int mWidth;

	public Block(int x, int y) {
		mY = y;
		mX = x;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(4);

		Random random = new Random();
		mWidth = random.nextInt(20);
		mHeight = random.nextInt(20);
	}

	@Override
	public void drawSelf(Canvas canvas) {
		computePosition();

		canvas.drawRect(mX, mY, mX + mWidth, mY - mHeight, mPaint);
	}

	private void computePosition() {
		mX -= MOVE_INTERVAL;
		if (mX <= 0) {
			fireOnBlockMoveOutOffScreen();
		}
	}

	private void fireOnBlockMoveOutOffScreen() {
		if (null != mBlockListener) {
			mBlockListener.onOutOfScreen(this);
		}
	}

	public void setBlockListener(BlockListener listener) {
		mBlockListener = listener;
	}

	public interface BlockListener {
		void onOutOfScreen(Block block);
	}

	@Override
	protected Rect getBounds() {
		return new Rect(mX, mY - mHeight, mX + mWidth, mY);
	}
}
