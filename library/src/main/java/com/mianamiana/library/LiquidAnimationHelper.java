package com.mianamiana.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Mianamiana on 15/9/24.
 */
public class LiquidAnimationHelper {


    private AnimatorSet animatorSet;
    private Animator.AnimatorListener animatorListener;
    private LiquidBallProgressBar mLiquidProgressBar;


    public Animator.AnimatorListener getAnimatorListener() {
        return animatorListener;
    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public LiquidAnimationHelper(LiquidBallProgressBar liquidProgressBar) {
        mLiquidProgressBar = liquidProgressBar;
    }

    public void startAnimation(int targetProgress) {
        // horizontal animation. 200 = wave.png width
        ObjectAnimator maskXAnimator = ObjectAnimator.ofFloat(mLiquidProgressBar, "waveTranslationX", 0, 200);
        maskXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        maskXAnimator.setDuration(600);
        maskXAnimator.setStartDelay(0);

        // vertical animation
        // maskY = 0 -> wave vertically centered
        // repeat mode REVERSE to go back and forth
        ObjectAnimator maskYAnimator = ObjectAnimator.ofInt(mLiquidProgressBar, "progress", targetProgress);
        maskYAnimator.setDuration(1000);
        maskYAnimator.setStartDelay(0);

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(maskXAnimator, maskYAnimator);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    public void cancelAnimation() {
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    public void endAnimation() {
        if (animatorSet != null) {
            animatorSet.end();
        }
    }


}
