package com.jelouodsa.littleions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class DrawingBall extends View{
	
	Bitmap heart;
	int x, y;

	public DrawingBall(Context context) {
		super(context);
		heart = BitmapFactory.decodeResource(getResources(), R.drawable.life);
		x = 0;
		y = 0;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Rect our_rect = new Rect();
		our_rect.set(0, 0, canvas.getWidth(), canvas.getHeight()/2);
		Paint blue = new Paint();
		blue.setColor(Color.BLUE);
		blue.setStyle(Paint.Style.FILL);
		canvas.drawRect(our_rect, blue);
		if(x < canvas.getWidth()){
			x+=10;
		}
		else{
			x = 0;
		}
		if(y < canvas.getHeight()){
			y+=10;
		}
		else{
			y = 0;
		}
		canvas.drawBitmap(heart, x, y, new Paint());
		invalidate();
	}

}
