package com.jelouodsa.littleions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class Game extends Activity implements OnTouchListener {

	DrawingBall ball;
	Bitmap monster, blob, heart;
	Bitmap trackBall, trackPad;
	MyView sBall;
	int x, y;
	int xt, yt;
	Sprite sprite;
	boolean flagis;
	Display display;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		display = getWindowManager().getDefaultDisplay();
		super.onCreate(savedInstanceState);

		monster = BitmapFactory.decodeResource(getResources(),
				R.drawable.monsters);
		blob = BitmapFactory.decodeResource(getResources(), R.drawable.image);
		heart = BitmapFactory.decodeResource(getResources(), R.drawable.life);
		trackBall = BitmapFactory.decodeResource(getResources(),
				R.drawable.ball);
		trackPad = BitmapFactory.decodeResource(getResources(),
				R.drawable.trackpad);
		sBall = new MyView(this);
		sBall.setOnTouchListener(this);

		x = 0;
		y = 0;
		setContentView(sBall);
		xt = trackPad.getWidth() / 2 - trackBall.getWidth() / 2 + 5;
		yt = display.getHeight() - trackPad.getHeight() / 2
				- trackBall.getHeight() / 2 - 5;
		flagis = false;

	}

	@Override
	protected void onResume() {
		super.onResume();
		sBall.resume();
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
			sprite = new Sprite(this, blob, monster);
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

			canvas.drawARGB(255, 0, 0, 255);
			canvas.drawBitmap(heart, x, y, new Paint());
			canvas.drawBitmap(trackPad, 20,
					canvas.getHeight() - trackPad.getHeight() - 20, new Paint());
			canvas.drawBitmap(trackBall, xt, yt, new Paint());
			sprite.onDraw(canvas);
			if (!flagis) {
				xt = trackPad.getWidth() / 2 - trackBall.getWidth() / 2 + 5;
				yt = display.getHeight() - trackPad.getHeight() / 2
						- trackBall.getHeight() / 2 - 5;
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
		if ((event.getY() <= display.getHeight() - trackBall.getHeight() - 20)
				&& (event.getY() >= display.getHeight() - trackPad.getHeight()
						- 20)
				&& (event.getX() <= trackPad.getWidth() - trackBall.getWidth()
						+ 20) && (event.getX() >= 20)) {
			xt = (int) event.getX();
			yt = (int) event.getY();
		} else if ((event.getY() > display.getHeight()/* 1 */
				- trackBall.getHeight() - 20)
				&& (event.getX() <= trackPad.getWidth() - trackBall.getWidth()
						+ 20) && (event.getX() >= 20)) {
			xt = (int) event.getX();
			yt = display.getHeight() - trackBall.getHeight() - 20;
			if ((y < display.getHeight() - heart.getHeight()- 1)) {
				y+=10;
			}
		} else if ((event.getY() <= display.getHeight() - trackBall.getHeight()/* 2 */
				- 20)
				&& (event.getY() >= display.getHeight() - trackPad.getHeight()
						- 20)
				&& (event.getX() > trackPad.getWidth() - trackBall.getWidth()
						+ 20)) {
			yt = (int) event.getY();
			xt = trackPad.getWidth() - trackBall.getWidth() + 20;
			if ((x < display.getWidth() - heart.getWidth()- 1)) {
				x+=10;
			}
		} else if ((event.getY() > display.getHeight()/* 3 */
				- trackBall.getHeight() - 20)
				&& (event.getX() > trackPad.getWidth() - trackBall.getWidth()
						+ 20)) {
			yt = display.getHeight() - trackBall.getHeight() - 20;
			xt = trackPad.getWidth() - trackBall.getWidth() + 20;
			if ((x < display.getWidth() - heart.getWidth() - 1)
					&& (y < display.getHeight() - heart.getHeight() - 1)) {
				x+=10;
				y+=10;
			}
		} else if ((event.getY() < display.getHeight()/* 4 */
				- trackPad.getHeight() - 20)
				&& (event.getX() > trackPad.getWidth() - trackBall.getWidth()
						+ 20)) {
			yt = display.getHeight() - trackPad.getHeight() - 20;
			xt = trackPad.getWidth() - trackBall.getWidth() + 10;
			if ((x < display.getWidth() - heart.getWidth() - 1) && (y > 1)) {
				x+=10;
				y-=10;
			}
		} else if ((event.getY() < display.getHeight()/* 5 */
				- trackPad.getHeight() - 20)
				&& (event.getX() <= trackPad.getWidth() - trackBall.getWidth()
						+ 20) && (event.getX() >= 20)) {
			xt = (int) event.getX();
			yt = display.getHeight() - trackPad.getHeight() - 20;
			if (y > 1) {
				y-=10;
			}
		} else if ((event.getY() <= display.getHeight() - trackBall.getHeight()/* 6 */
				- 20)
				&& (event.getY() >= display.getHeight() - trackPad.getHeight()
						- 20) && (event.getX() < 20)) {
			yt = (int) event.getY();
			xt = 20;
			if (x > 1) {
				x-=10;
			}
		} else if ((event.getY() < display.getHeight()/* 7 */
				- trackPad.getHeight() - 20) && (event.getX() < 20)) {
			yt = display.getHeight() - trackPad.getHeight() - 20;
			xt = 20;
			if (x > 1 && y > 1) {
				x-=10;
				y-=10;
			}
		} else {
			yt = display.getHeight() - trackBall.getHeight() - 20;
			xt = 20;
			if (x > 1 && (y < display.getHeight() - heart.getHeight() - 1)) {
				x--;
				y+=10;
			}
		}

	}
}
