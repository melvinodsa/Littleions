package com.jelouodsa.littleions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements SensorListener {

	private SensorManager mSensorManager = null;
	private Button play;
	private boolean check;
	private float ini;
	private float upd;
	private float accX;
	private float prevX;
	private MediaPlayer play_sound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		check = false;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.menu);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		Typeface high_school = Typeface.createFromAsset(getAssets(),
				"fonts/high_school.ttf");
		play = (Button) findViewById(R.id.play_btn);
		play.setTypeface(high_school);
		play.setTextColor(Color.WHITE);
		play.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				play_sound = MediaPlayer.create(MainActivity.this, R.raw.button);
				play_sound.start();
				Thread game = new Thread(){
					public void run(){
						try{
							sleep(50);
							startActivity(new Intent("com.jelouodsa.littleions.GAME"));
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally{
							finish();
						}
					}
				};
				game.start();
			}
		});

	}

	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(int sensor, float[] values) {
		synchronized (this) {

			if (sensor == SensorManager.SENSOR_ORIENTATION) {

			}
			if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
				upd = values[0];
				if (!check) {
					ini = values[0];
					check = true;
					accX = (float) (200.0 + (upd - ini) * 50);
					prevX = accX;
				}
				accX = (float) (200.0 + (upd - ini) * 50);

				if (accX - prevX > 10 || accX - prevX < -10) {
					if (accX < 600 && accX > 0) {
						play.setX(accX);
						Log.i("MainActivity", Float.toString(accX));
					}
				}
				prevX = accX;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, SensorManager.SENSOR_ORIENTATION
				| SensorManager.SENSOR_ACCELEROMETER,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		play_sound.release();
		super.onPause();
	}

}
