package com.vinson.dietogether.model;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class RunningMan extends AbstractShape {

	private static final int POSITION_INTERVAL = 2;
	private static final int JUMP_HEIGHT = 50;
	private int mJumpHeight = 0;

	private Paint mPaint;
	private Random mRandom;
	private JumpState mJumpState = JumpState.NONE;

	public RunningMan(int x, int y) {

		mX = x;
		mY = y;

		mRandom = new Random();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(4);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Style.STROKE);
	}

	public void jump() {
		if (mJumpState == JumpState.NONE) {
			mJumpState = JumpState.RASING;
		}
	}

	@Override
	public void drawSelf(Canvas canvas) {
		calculatePosition();
		canvas.drawRect(mX, mY - mJumpHeight, mX + 20, mY - mJumpHeight - 20, mPaint);
	}

	private void calculatePosition() {
		switch (mJumpState) {
		case NONE:

			break;
		case RASING:
			mJumpHeight += POSITION_INTERVAL;
			if (mJumpHeight >= JUMP_HEIGHT) {
				mJumpState = JumpState.DROPING;
			}
			break;

		case DROPING:
			mJumpHeight -= POSITION_INTERVAL;
			if (mJumpHeight <= 0) {
				mJumpState = JumpState.NONE;
			}
			break;

		default:
			break;
		}
	}

	public boolean isConflickWith(AbstractShape shape) {
		Rect rect = getBounds();
		Rect otherRect = shape.getBounds();

		return Rect.intersects(rect, otherRect);
	}

	public enum JumpState {
		RASING, DROPING, NONE
	}

	@Override
	protected Rect getBounds() {
		return new Rect(mX, mY - mJumpHeight - 20, mX + 20, mY - mJumpHeight);
	}
}
