package com.sm.testtictac;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

public class MainActivity extends Activity {
	public BoardView gameBoardView;
	public FrameLayout container;
	private ScrollView vSc;
	private HorizontalScrollView hSc;
	
    @SuppressLint("ClickableViewAccessibility") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameBoardView = (BoardView) findViewById(R.id.newGameBoardView);
		gameBoardView.setGame(this);
		gameBoardView.setTouchInput(true);
		lastScaled = gameBoardView.mScaleBoard;

		final ScaleGestureDetector detector = new ScaleGestureDetector(this, new ScaleListener()); 
		container = (FrameLayout) findViewById(R.id.gameBoardContainer);

		vSc = (ScrollView)findViewById(R.id.verticalScrollView);
		vSc.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				detector.onTouchEvent(event);
				return false;
			}
		});
		hSc = (HorizontalScrollView)findViewById(R.id.horizantalScrollView);
		hSc.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
	            detector.onTouchEvent(event);
				return false;
			}
		});
	}
	
	public void updateScaledBoard(final float focusX, final float focusY) {
		if (Double.valueOf(lastScaled).equals(Double.valueOf(gameBoardView.mScaleBoard))) return;
		lastScaled = gameBoardView.mScaleBoard;

		Log.i("SCALE:", "CELL_WIDTH:" +(int)((gameBoardView.getWidth()/gameBoardView.getRowColSize()) * gameBoardView.mScaleBoard));
		
		gameBoardView.setCellSize((int)(50 * gameBoardView.mScaleBoard));
		container.requestLayout();
		gameBoardView.requestLayout();
	}

	public float MAX_SCALE = 3.0f;
	public float MIN_SCALE = 1.0f;
	public float lastScaled = 1f;
	public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			gameBoardView.mScaleBoard *= detector.getScaleFactor();
			gameBoardView.mScaleBoard = Math.max(MIN_SCALE, Math.min(gameBoardView.mScaleBoard, MAX_SCALE));
	        Log.i("ACT", "scale:" + gameBoardView.mScaleBoard);
	        updateScaledBoard(detector.getFocusX(), detector.getFocusY());
			return super.onScale(detector);
		}
	}
}
