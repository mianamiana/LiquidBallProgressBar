
/*
 * Copyright  2015 Mianamiana
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mianamiana.library;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Mianamiana on 15/9/20.
 */
public class LiquidBallProgressBar extends FrameLayout {

    private Paint mWavePaint;
    private float mWaveTranslationX;
    private float mWaveTranslationY;
    private int mWaveOffsetY;
    private Matrix mWaveShaderMatrix;
    private boolean mIsRunAnimation;
    private LiquidTextView mLiquidTextView;
    private BitmapShader mWaveShader;
    private LiquidAnimationHelper mAnimationHelper;
    private int mWaveH;
    private int mWaveW;
    private Drawable mWave;
    private int mProgress;
    private int mMaxProgress = 100;
    private int mUnfilledWaveColor = 0xfff6f6fc;
    private int mWaveColor = 0xffa2de5a;
    private Paint mBorderPaint;
    private int mBorderWidth = 0;
    private int mBorderColor = 0;
    private int mTextOverlapColor = 0xfff6f6fc;
    private int mTextColor = 0xffa2de5a;
    private OnProgressChangeListener mOnProgressChangeListener;
    private float mTextSize = 20;


    public interface OnProgressChangeListener {
        void onProgressChange(View view, int progress);
    }

    public LiquidBallProgressBar(Context context) {
        this(context, null, 0);
    }

    public LiquidBallProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiquidBallProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        // animation helper
        mAnimationHelper = new LiquidAnimationHelper(this);

        //init  paints
        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWaveShaderMatrix = new Matrix();

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);


        //add LiquidTextView
        mLiquidTextView = new LiquidTextView(context);
        mLiquidTextView.setText(mProgress + "");
        mLiquidTextView.setTextSize(mTextSize);
        mLiquidTextView.setTextColor(mTextColor);
        mLiquidTextView.setGravity(Gravity.CENTER);
        addView(mLiquidTextView, generateDefaultLayoutParams());


        // wave drawable
        mWave = getResources().getDrawable(R.drawable.wave);
        mWave.mutate();
        mWaveW = mWave.getIntrinsicWidth();
        mWaveH = mWave.getIntrinsicHeight();


        //======
        setMaxProgress(mMaxProgress);
        setProgress(mProgress);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LiquidBallProgressBar);
        try {
            mBorderColor = a.getColor(R.styleable.LiquidBallProgressBar_ballBorderColor, mBorderColor);
            mTextColor = a.getColor(R.styleable.LiquidBallProgressBar_textColor, mTextColor);
            mWaveColor = a.getColor(R.styleable.LiquidBallProgressBar_waveColor, mWaveColor);
            mUnfilledWaveColor = a.getColor(R.styleable.LiquidBallProgressBar_unfilledColor, mUnfilledWaveColor);
            mTextOverlapColor = a.getColor(R.styleable.LiquidBallProgressBar_textOverlapColor, mTextOverlapColor);
            int textsize = a.getDimensionPixelSize(R.styleable.LiquidBallProgressBar_textSize, 0);
            if(textsize!=0)
            {
                mTextSize =textsize/ context.getResources().getDisplayMetrics().scaledDensity;
            }
            mBorderWidth = (int) (a.getDimension(R.styleable.LiquidBallProgressBar_ballBorderWidth, mBorderWidth) + 0.5f);
            mProgress = a.getInteger(R.styleable.LiquidBallProgressBar_progress, mProgress);
            mMaxProgress = a.getInteger(R.styleable.LiquidBallProgressBar_maxProgress, mMaxProgress);
        } finally {
            a.recycle();
        }
    }


    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.mOnProgressChangeListener = onProgressChangeListener;
    }

    public int getTextOverlapColor() {
        return mTextOverlapColor;
    }

    public void setTextOverlapColor(int textOverlapColor) {
        this.mTextOverlapColor = textOverlapColor;
        mLiquidTextView.resetShader();
    }

    public void setText(String text) {
        mLiquidTextView.setText(text);
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.mBorderWidth = borderWidth;
        resetShader();
        mLiquidTextView.resetShader();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
    }

    public void setTextSize(float size) {
        mLiquidTextView.setTextSize(size);
    }

    public void setTypeface(Typeface tf)
    {
        mLiquidTextView.setTypeface(tf);
    }

    public void setTextColor(int color) {
        mLiquidTextView.setTextColor(color);
        mLiquidTextView.resetShader();
    }

    public void setTextColor(ColorStateList colors) {
        mLiquidTextView.setTextColor(colors);
        mLiquidTextView.resetShader();
    }

    public int getUnfilledWaveColor() {
        return mUnfilledWaveColor;
    }

    public void setUnfilledWaveColor(int unfilledWaveColor) {
        mUnfilledWaveColor = unfilledWaveColor;
        resetShader();
    }

    public int getWaveColor() {
        return mWaveColor;
    }

    public void setWaveColor(int waveColor) {
        mWaveColor = waveColor;
        resetShader();
        mLiquidTextView.resetShader();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        return lp;
    }

    //Be a quadrate view
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWithSize = getMeasuredWidth();
        heightMeasureSpec = widthMeasureSpec =
                MeasureSpec.makeMeasureSpec(childWithSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private BitmapShader createShader(int foregroundColor, int backgroundColor) {

        DrawableCompat.setTint(mWave, foregroundColor);

        Bitmap b = Bitmap.createBitmap(mWaveW, mWaveH, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(backgroundColor);

        mWave.setBounds(0, 0, mWaveW, mWaveH);
        mWave.draw(c);

        BitmapShader shader = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);

        return shader;
    }

    private BitmapShader getWaveShader() {
        return createShader(mWaveColor, mUnfilledWaveColor);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetShader();
    }

    private void resetShader() {

        mWaveOffsetY = getHeight() - mWaveH / 2 - mBorderWidth;
        mWaveShader = getWaveShader();
        mWaveShaderMatrix.setTranslate(mWaveTranslationX, mWaveOffsetY - mWaveTranslationY);
        mWaveShader.setLocalMatrix(mWaveShaderMatrix);
        mWavePaint.setShader(mWaveShader);


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (getBackground() == null) {
            drawLiqiud(canvas);
        }
        super.dispatchDraw(canvas);
    }


    @Override
    public void draw(Canvas canvas) {
        drawLiqiud(canvas);
        super.draw(canvas);
    }

    private void drawLiqiud(Canvas canvas) {
        float radius = getWidth() / 2.0f;
        float cx = getWidth() / 2.0f;
        float cy = getHeight() / 2.0f;

        if (mIsRunAnimation) {
            mWaveShaderMatrix.setTranslate(mWaveTranslationX, mWaveOffsetY + mWaveTranslationY);
            mWaveShader.setLocalMatrix(mWaveShaderMatrix);
        }
        if (mBorderWidth != 0) {
            canvas.drawCircle(cx, cy, radius, mBorderPaint);
            canvas.drawCircle(cx, cy, radius - mBorderWidth, mWavePaint);
        } else {
            canvas.drawCircle(cx, cy, radius, mWavePaint);
        }


    }


    public void setProgressWithAnimation(int targetProgress) {
        targetProgress = Math.max(Math.min(targetProgress, mMaxProgress), 0);
        mIsRunAnimation = true;
        mAnimationHelper.startAnimation(targetProgress);
    }

    /**
     * start wave animation
     */
    public void startWave() {
        mIsRunAnimation = true;
        mAnimationHelper.startAnimation(getProgress());
    }


    /**
     * cancel wave animation ;
     */
    public void cancelWave() {
        mIsRunAnimation = false;
        mAnimationHelper.cancelAnimation();
    }


    /**
     * Ends the wave. This causes the animation to assign the target progess
     */
    public void endWave() {
        mAnimationHelper.endAnimation();
        mIsRunAnimation = false;
    }

    public float getWaveTranslationX() {
        return mWaveTranslationX;
    }

    public void setWaveTranslationX(float waveTranslationX) {
        this.mWaveTranslationX = waveTranslationX;
        invalidate();
    }


    @Override
    public void invalidate() {
        super.invalidate();
        mLiquidTextView.invalidate();
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
        //dent of wave.png = 20
        mWaveTranslationY = -(getHeight() + 20 - mBorderWidth * 2) * mProgress / mMaxProgress;
        if (mOnProgressChangeListener != null) {
            mOnProgressChangeListener.onProgressChange(this, progress);
        } else {
            setText(progress + "");
        }
        invalidate();
    }

    ///////////////////////////////////////////////////////////////////////////
    // render text view
    ///////////////////////////////////////////////////////////////////////////
    private class LiquidTextView extends TextView {
        private int offsetY;
        private Matrix textShaderMatrix;
        private BitmapShader textShader;

        public LiquidTextView(Context context) {
            super(context);
            textShaderMatrix = new Matrix();
        }


        private BitmapShader getTextShader() {
            return createShader(mTextOverlapColor, getCurrentTextColor());
        }


        @Override
        protected void onDraw(Canvas canvas) {
            if (mIsRunAnimation) {
                textShaderMatrix.setTranslate(mWaveTranslationX, offsetY + mWaveTranslationY);
                textShader.setLocalMatrix(textShaderMatrix);
            }
            super.onDraw(canvas);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            resetShader();
        }

        private void resetShader() {

            offsetY = LiquidBallProgressBar.this.getHeight() - LiquidTextView.this.getTop() - mWaveH / 2 -
                    mBorderWidth;
            textShader = getTextShader();
            textShaderMatrix.setTranslate(mWaveTranslationX, offsetY + mWaveTranslationY);
            textShader.setLocalMatrix(textShaderMatrix);
            getPaint().setShader(textShader);

        }


    }


}
