package com.vinson.dietogether.model;

import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class AbstractShape {

	protected int mX;
	protected int mY;

	public void setX(int x) {
		mX = x;
	}

	public void setY(int y) {
		mY = y;
	}

	public abstract void drawSelf(Canvas canvas);

	protected abstract Rect getBounds();
}
