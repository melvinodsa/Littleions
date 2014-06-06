package com.jelouodsa.littleions;

import com.jelouodsa.littleions.Game.MyView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {

	int x, y;
	int ym;
	int xSpeed, ySpeed;
	int ySpeedM;
	int height, width;
	int heightm, widthm;
	Bitmap b, m;
	MyView mv;
	int direction = 2;
	int currentFrame = 0;
	int currentFrameM = 0;
	int directionM = 0;

	public Sprite(MyView myView, Bitmap blob, Bitmap monster) {
		b = blob;
		m = monster;
		mv = myView;
		width = b.getWidth() / 3;
		height = b.getHeight() / 4;
		widthm = m.getWidth() / 3;
		heightm = m.getHeight() / 2;
		x = y = 0;
		ym = 0;
		xSpeed = 5;
		ySpeed = 0;
		ySpeedM = 10;
	}

	public void onDraw(Canvas canvas) {
		update();
		int srcX = currentFrame * width;
		int srcY = direction * height;
		int srcXM = currentFrameM * widthm;
		int srcYM = directionM * heightm;
		int srcY1M = Math.abs(directionM - 1) * heightm;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x, y, x + width, y + height);
		Rect srcM = new Rect(srcXM,srcYM, srcXM + widthm, srcYM + heightm);
		Rect src1M = new Rect(srcXM,srcY1M, srcXM + widthm, srcY1M + heightm);
		Rect dstM = new Rect(200, ym, 200+widthm, ym + heightm);
		Rect dst1M = new Rect(400, Math.abs(mv.getHeight() - ym) - heightm, 400+widthm, Math.abs(mv.getHeight() - ym));
		Rect dst2M = new Rect(600, ym, 600+widthm, ym + heightm);
		canvas.drawBitmap(m, src1M, dst1M, null);
		canvas.drawBitmap(m, srcM, dst2M, null);
		canvas.drawBitmap(b, src, dst, null);
		canvas.drawBitmap(m, srcM, dstM, null);

	}

	private void update() {

		if (ym > mv.getHeight() - heightm - ySpeedM) {
			ySpeedM = -10;
			directionM = 1;
		}
		if (ym + ySpeedM < 0) {
			ym = 0;
			ySpeedM = 10;
			directionM = 0;
		}

		if (x > mv.getWidth() - width - xSpeed) {
			xSpeed = 0;
			ySpeed = 5;
			direction = 0;
		}
		if (y > mv.getHeight() - height - ySpeed) {
			xSpeed = -5;
			ySpeed = 0;
			direction = 1;
		}
		if (x + xSpeed < 0) {
			x = 0;
			xSpeed = 0;
			ySpeed = -5;
			direction = 3;
		}
		if (y + ySpeed < 0) {
			y = 0;
			xSpeed = 5;
			ySpeed = 0;
			direction = 2;
		}

		currentFrame = ++currentFrame % 3;
		currentFrameM = ++currentFrameM % 3;
		ym += ySpeedM;
		x += xSpeed;
		y += ySpeed;
	}

}
