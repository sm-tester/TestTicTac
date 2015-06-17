package com.sm.testtictac;

import java.lang.reflect.Array;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View implements View.OnTouchListener, View.OnClickListener {
	private static final String TAG = BoardView.class.getSimpleName();
	private int mBoardSize;
	private int mCellSize;
	private int mCurrentPlayer;
	private int[][] mData;
	public Point mLastPosition;
	public float mLastXPosition = -1.0F;
	public float mLastYPosition = -1.0F;
	private Bitmap mOCIcon;
	private Bitmap mOIcon;
	private GoToPointListener mOnGoToPointListener;
	private OnWinListener mOnWinListener;
	private Paint mPaint;
	private Paint mPaintLine;
	public float mScaleBoard = 1.0F;
	private boolean mTouchInput = true;
	private Bitmap mXCIcon;
	private Bitmap mXIcon;

	public MainActivity game;

	public BoardView(Context paramContext) {
		super(paramContext);
		init();
	}

	public BoardView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init();
	}

	public BoardView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		init();
	}

	public void setGame(MainActivity game) {
		this.game = game;
	}

	public Point convertPointToPosition(Point paramPoint) {
		return new Point(paramPoint.getY() * this.mCellSize, paramPoint.getX() * this.mCellSize);
	}
	public Point convertPositionToPoint(float paramFloat1, float paramFloat2) {
		return new Point((int)(paramFloat2 / this.mCellSize), (int)(paramFloat1 / this.mCellSize));
	}

	public void scrollToLastMove() {
		scrollTo((int)mLastXPosition, (int)mLastYPosition);
	}
	
	private void drawBoard(Canvas canvas) {
		for (int i = 0; i < mBoardSize; i++) {
			canvas.drawLine(mCellSize * i, 0, mCellSize * i, getBoardSize(), mPaint);
			canvas.drawLine(0, mCellSize * i, getBoardSize(), mCellSize * i, mPaint);
		}
	}

	private void drawXOPosition(Canvas canvas) {
		for (int i = 0; i < mData.length; i++) {
			for (int j = 0; j < mData.length; j++) {
				if (mData[i][j] > 0) {
					drawPoint(canvas, new Point(i, j), this.mData[i][j]);
				}
			}
		}
	}

	private Bitmap getScaleBitmap(Bitmap paramBitmap, float paramFloat) {
		if ((paramFloat == 1.0F) || (paramFloat <= 0.0F) || (paramBitmap == null)) {
			return paramBitmap;
		}
		return Bitmap.createScaledBitmap(paramBitmap, Math.max(1, (int)(paramBitmap.getWidth() * paramFloat)), Math.max(1, (int)(paramBitmap.getHeight() * paramFloat)), true);
	}

	private void initIcons() {
		this.mXIcon = BitmapFactory.decodeResource(getResources(), R.drawable.x);
		this.mXCIcon = BitmapFactory.decodeResource(getResources(), R.drawable.xc);
		this.mOIcon = BitmapFactory.decodeResource(getResources(), R.drawable.o);
		this.mOCIcon = BitmapFactory.decodeResource(getResources(), R.drawable.oc);

		float f = (this.mCellSize - 6) * 1.0F / (getXOSize() * 1.0F);

		if ((f != 1.0F) && (f > 0.0F)) {
			this.mXIcon = getScaleBitmap(this.mXIcon, f);
			this.mXCIcon = getScaleBitmap(this.mXCIcon, f);
			this.mOIcon = getScaleBitmap(this.mOIcon, f);
			this.mOCIcon = getScaleBitmap(this.mOCIcon, f);
		}
	}

	private void togglePlayer() {
		if (this.mCurrentPlayer == 1)
		{
			this.mCurrentPlayer = 2;
			return;
		}
		this.mCurrentPlayer = 1;
	}

	private void updateStatus(Point paramPoint) {
		if ((isFinish()) && (this.mOnWinListener != null)) {
			this.mOnWinListener.onWin(this.mData[paramPoint.getX()][paramPoint.getY()]);
		}
	}

	protected void drawLineWin(Canvas paramCanvas){}

	protected boolean drawPoint(Canvas canvas, Point point, int paramInt) {
		Point point2 = convertPointToPosition(point);
		float f = (float)(mCellSize - getXOSize()) / 2.0F;
		canvas.drawBitmap(mOIcon, f + (float)point2.getX(), f + (float)point2.getY(), null);
		return true;
	}

	public final int getBoardSize() {
		return this.mBoardSize * this.mCellSize;
	}

	public int getCellSize() {
		return this.mCellSize;
	}

	public int getCurrentPlayer() {
		return this.mCurrentPlayer;
	}

	public int[][] getData() {
		return this.mData;
	}

	public final int getRowColSize() {
		return this.mBoardSize;
	}

	public float getScaleBoard() {
		return this.mScaleBoard;
	}

	protected int getXOSize() {
		if (this.mXIcon == null) {
			return 0;
		}
		return Math.max(this.mXIcon.getWidth(), this.mXIcon.getHeight());
	}

	public boolean gotoPoint(Point paramPoint, int paramInt) {
		if (this.mData[paramPoint.getX()][paramPoint.getY()] != 0) {
			return false;
		}
		togglePlayer();
		this.mLastPosition = paramPoint;
		this.mData[paramPoint.getX()][paramPoint.getY()] = paramInt;

		updateStatus(paramPoint);

		if (this.mOnGoToPointListener != null) {
			this.mOnGoToPointListener.onGoToPoint(paramInt, paramPoint.getX(), paramPoint.getY());
		}
		invalidate();
		return true;
	}

	@SuppressLint("ClickableViewAccessibility") 
	protected void init() {
		this.mCellSize = 50;
		this.mBoardSize = 50;
		this.mPaint = new Paint();
		this.mPaint.setAntiAlias(true);
		this.mPaint.setColor(-65536);
		this.mPaint.setStrokeWidth(1.0F);
		this.mPaintLine = new Paint();
		this.mPaintLine.setAntiAlias(true);
		this.mPaintLine.setColor(-65536);
		this.mPaintLine.setStrokeWidth(5.0F);
		setOnTouchListener(this);
		setOnClickListener(this);
		initIcons();
		try {
			resetDataBoard();
			return;
		}
		catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public boolean isFinish() {
		return false;
	}

	public boolean isTouchInput() {
		return this.mTouchInput;
	}

	public void onClick(View paramView) {
		Log.i(TAG, "onClick isTouchInput:" + isTouchInput());
		if (!isTouchInput()) {
			return;
		}
		Point point = convertPositionToPoint(mLastXPosition, mLastYPosition);
		Log.i(TAG, (new StringBuilder("position: ")).append(mLastXPosition).append(",").append(mLastYPosition).toString());
		Log.i(TAG, point.toString());
		gotoPoint(point, getCurrentPlayer());
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBoard(canvas);
		drawXOPosition(canvas);
		if (isFinish()) {
			drawLineWin(canvas);
		}
	}

	protected void onMeasure(int width, int height) {
		super.onMeasure(width, height);
		setMeasuredDimension(getBoardSize(), getBoardSize());
	}

	@SuppressLint("ClickableViewAccessibility") 
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == 0) {
			this.mLastXPosition = event.getX();
			this.mLastYPosition = event.getY();
		}
		return false;
	}

	public void resetDataBoard() throws Exception {
		if ((this.mBoardSize <= 0) || (this.mBoardSize <= 0)) {
			throw new Exception("Invalid row, column value");
		}
		this.mCurrentPlayer = 1;
		int i = this.mBoardSize;
		int j = this.mBoardSize;
		this.mData = ((int[][])Array.newInstance(Integer.TYPE, new int[] { i, j }));
	}

	public void setBoardSize(int paramInt) {
		this.mBoardSize = paramInt;
	}

	public void setCellSize(int paramInt) {
		this.mCellSize = paramInt;
		initIcons();
		invalidate();
	}

	public void setOnGoToPointListener(GoToPointListener paramGoToPointListener) {
		this.mOnGoToPointListener = paramGoToPointListener;
	}

	public void setOnWinListener(OnWinListener paramOnWinListener) {
		this.mOnWinListener = paramOnWinListener;
	}

	public void setTouchInput(boolean paramBoolean) {
		this.mTouchInput = paramBoolean;
	}

	public static abstract interface GoToPointListener {
		public abstract void onFillAllCell();

		public abstract void onGoToPoint(int paramInt1, int paramInt2, int paramInt3);
	}

	public static abstract interface OnWinListener {
		public abstract void onWin(int paramInt);
	}
}