package com.jelouodsa.littleions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class Game extends Activity implements OnTouchListener {

	DrawingBall ball;
	Bitmap monster, blob, heart, background;
	Bitmap trackBall, trackPad;
	MyView sBall;
	int x, y;
	int xt, yt;
	Sprite sprite;
	boolean flagis;
	Display display;
	Boolean hash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		display = getWindowManager().getDefaultDisplay();
		super.onCreate(savedInstanceState);

		monster = BitmapFactory.decodeResource(getResources(),
				R.drawable.monsters);
		blob = BitmapFactory.decodeResource(getResources(), R.drawable.image);
		heart = BitmapFactory.decodeResource(getResources(), R.drawable.life);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.back);
		trackBall = BitmapFactory.decodeResource(getResources(),
				R.drawable.ball);
		trackPad = BitmapFactory.decodeResource(getResources(),
				R.drawable.trackpad);
		sBall = new MyView(this);
		sBall.setOnTouchListener(this);

		x = display.getWidth() - heart.getWidth();
		y = display.getHeight() - heart.getHeight();
		setContentView(sBall);
		xt = trackPad.getWidth() / 2 - trackBall.getWidth() / 2 + 35;
		yt = display.getHeight() - trackPad.getHeight() / 2
				- trackBall.getHeight() / 2 - 35;
		flagis = false;
		hash = true;

	}

	@Override
	protected void onResume() {
		super.onResume();
		sBall.resume();
	}

	public class Sprite {

		int x, y;
		int ym;
		int xSpeed, ySpeed;
		int ySpeedM;
		int height, width;
		int heightm, widthm;
		Bitmap b, m, h;
		MyView mv;
		int direction = 3;
		int currentFrame = 0;
		int currentFrameM = 0;
		int directionM = 0;
		int checkX, checkY;

		public Sprite(MyView myView, Bitmap blob, Bitmap monster, Bitmap heart) {
			b = blob;
			m = monster;
			mv = myView;
			h = heart;
			width = b.getWidth() / 9;
			height = b.getHeight() / 4;
			widthm = m.getWidth() / 3;
			heightm = m.getHeight() / 2;
			x = y = 0;
			ym = 0;
			xSpeed = 5;
			ySpeed = 0;
			ySpeedM = 10;
		}

		public boolean onDraw(Canvas canvas, int checkX, int checkY) {

			update();
			boolean ch;
			int srcX = currentFrame * width;
			int srcY = direction * height;
			int srcXM = currentFrameM * widthm;
			int srcYM = directionM * heightm;
			int srcY1M = Math.abs(directionM - 1) * heightm;
			Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
			Rect dst = new Rect(x, y, x + width, y + height);
			Rect srcM = new Rect(srcXM, srcYM, srcXM + widthm, srcYM + heightm);
			Rect src1M = new Rect(srcXM, srcY1M, srcXM + widthm, srcY1M
					+ heightm);
			Rect dstM = new Rect(200, ym, 200 + widthm, ym + heightm);
			Rect dst1M = new Rect(400, Math.abs(mv.getHeight() - ym) - heightm,
					400 + widthm, Math.abs(mv.getHeight() - ym));
			Rect dst2M = new Rect(600, ym, 600 + widthm, ym + heightm);
			Rect check = new Rect(checkX, checkY, checkX+h.getWidth(), checkY+h.getHeight());
			canvas.drawBitmap(m, src1M, dst1M, null);
			canvas.drawBitmap(m, srcM, dst2M, null);
			canvas.drawBitmap(b, src, dst, null);
			canvas.drawBitmap(m, srcM, dstM, null);
			if (check.intersect(dst1M) || check.intersect(dst2M)
					|| check.intersect(dstM) || check.intersect(dst)) {
				ch = false;
			} else {
				ch = true;
			}
			return ch;

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
				direction = 2;
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
				direction = 0;
			}
			if (y + ySpeed < 0) {
				y = 0;
				xSpeed = 5;
				ySpeed = 0;
				direction = 3;
			}

			currentFrame = ++currentFrame % 9;
			currentFrameM = ++currentFrameM % 3;
			ym += ySpeedM;
			x += xSpeed;
			y += ySpeed;
		}

	}

	public class MyView extends SurfaceView implements Runnable {

		Thread t = null;
		SurfaceHolder holder;
		boolean flag = false;

		public MyView(Context context) {
			super(context);
			holder = getHolder();
		}

		@Override
		public void run() {
			sprite = new Sprite(this, blob, monster, heart);
			while (flag == true) {
				if (!holder.getSurface().isValid()) {
					continue;
				}

				Canvas c = holder.lockCanvas();
				onDrawit(c);
				holder.unlockCanvasAndPost(c);
			}
		}

		@SuppressLint("WrongCall")
		protected void onDrawit(Canvas canvas) {
				canvas.drawBitmap(background, 0, 0, new Paint());
				canvas.drawBitmap(heart, x, y, new Paint());
				canvas.drawBitmap(trackPad, 50,
						canvas.getHeight() - trackPad.getHeight() - 50,
						new Paint());
				canvas.drawBitmap(trackBall, xt, yt, new Paint());
				hash = sprite.onDraw(canvas, x, y);
				if(!hash){
					canvas.drawARGB(255, 255, 255, 255);
				}
				if (!flagis) {
					xt = trackPad.getWidth() / 2 - trackBall.getWidth() / 2
							+ 35;
					yt = display.getHeight() - trackPad.getHeight() / 2
							- trackBall.getHeight() / 2 - 35;
				}
				flagis = false;

		}

		public void pause() {
			flag = false;
			while (true) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			t = null;
		}

		public void resume() {
			flag = true;
			t = new Thread(this);
			t.start();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		sBall.pause();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		flagis = true;

		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			linkXY(display, trackPad, trackBall, event);
			break;
		case MotionEvent.ACTION_UP:
			linkXY(display, trackPad, trackBall, event);
			break;
		case MotionEvent.ACTION_MOVE:
			linkXY(display, trackPad, trackBall, event);
			break;
		}

		return true;
	}

	protected void linkXY(Display display, Bitmap trackPad, Bitmap trackBall,
			MotionEvent event) {
		if ((event.getY() <= display.getHeight() - trackBall.getHeight() - 50)
				&& (event.getY() >= display.getHeight() - trackPad.getHeight()
						- 50)
				&& (event.getX() <= trackPad.getWidth() - trackBall.getWidth()
						+ 50) && (event.getX() >= 50)) {
			xt = (int) event.getX();
			yt = (int) event.getY();
		} else if ((event.getY() > display.getHeight()/* 1 */
				- trackBall.getHeight() - 50)
				&& (event.getX() <= trackPad.getWidth() - trackBall.getWidth()
						+ 50) && (event.getX() >= 50)) {
			xt = (int) event.getX();
			yt = display.getHeight() - trackBall.getHeight() - 50;
			if ((y < display.getHeight() - heart.getHeight() - 1)) {
				y += 10;
			}
		} else if ((event.getY() <= display.getHeight() - trackBall.getHeight()/* 2 */
				- 50)
				&& (event.getY() >= display.getHeight() - trackPad.getHeight()
						- 50)
				&& (event.getX() > trackPad.getWidth() - trackBall.getWidth()
						+ 50)) {
			yt = (int) event.getY();
			xt = trackPad.getWidth() - trackBall.getWidth() + 50;
			if ((x < display.getWidth() - heart.getWidth() - 1)) {
				x += 10;
			}
		} else if ((event.getY() > display.getHeight()/* 3 */
				- trackBall.getHeight() - 50)
				&& (event.getX() > trackPad.getWidth() - trackBall.getWidth()
						+ 50)) {
			yt = display.getHeight() - trackBall.getHeight() - 50;
			xt = trackPad.getWidth() - trackBall.getWidth() + 50;
			if ((x < display.getWidth() - heart.getWidth() - 1)
					&& (y < display.getHeight() - heart.getHeight() - 1)) {
				x += 10;
				y += 10;
			}
		} else if ((event.getY() < display.getHeight()/* 4 */
				- trackPad.getHeight() - 50)
				&& (event.getX() > trackPad.getWidth() - trackBall.getWidth()
						+ 50)) {
			yt = display.getHeight() - trackPad.getHeight() - 50;
			xt = trackPad.getWidth() - trackBall.getWidth() + 50;
			if ((x < display.getWidth() - heart.getWidth() - 1) && (y > 1)) {
				x += 10;
				y -= 10;
			}
		} else if ((event.getY() < display.getHeight()/* 5 */
				- trackPad.getHeight() - 50)
				&& (event.getX() <= trackPad.getWidth() - trackBall.getWidth()
						+ 50) && (event.getX() >= 50)) {
			xt = (int) event.getX();
			yt = display.getHeight() - trackPad.getHeight() - 50;
			if (y > 1) {
				y -= 10;
			}
		} else if ((event.getY() <= display.getHeight() - trackBall.getHeight()/* 6 */
				- 50)
				&& (event.getY() >= display.getHeight() - trackPad.getHeight()
						- 50) && (event.getX() < 50)) {
			yt = (int) event.getY();
			xt = 50;
			if (x > 1) {
				x -= 10;
			}
		} else if ((event.getY() < display.getHeight()/* 7 */
				- trackPad.getHeight() - 50) && (event.getX() < 50)) {
			yt = display.getHeight() - trackPad.getHeight() - 50;
			xt = 50;
			if (x > 1 && y > 1) {
				x -= 10;
				y -= 10;
			}
		} else {
			yt = display.getHeight() - trackBall.getHeight() - 50;
			xt = 50;
			if (x > 1 && (y < display.getHeight() - heart.getHeight() - 1)) {
				x--;
				y += 10;
			}
		}

	}
}
