package com.myPuzzle.vuzzle;

import android.graphics.drawable.Animatable;
import android.os.Handler;

public class AVDWrapper {

    private Handler mHandler;
    private Animatable mDrawable;
    private  Callback mCallback;
    private Runnable mAnimationDoneRunnable = new Runnable() {

        @Override
        public void run() {
            if (mCallback != null)
                mCallback.onAnimationDone();
        }
    };

    public interface Callback {
        public void onAnimationDone();
        public void onAnimationStopped();
    }

    public AVDWrapper(Animatable drawable,
                      Handler handler,  AVDWrapper.Callback callback) {
        mDrawable = drawable;
        mHandler = handler;
        mCallback = callback;
    }

    // Duration of the animation
    public void start(long duration) {
        mDrawable.start();
        mHandler.postDelayed(mAnimationDoneRunnable, duration);
    }

    public void stop() {
        mDrawable.stop();
        mHandler.removeCallbacks(mAnimationDoneRunnable);

        if (mCallback != null)
            mCallback.onAnimationStopped();
    }
}


/**Your code would look like:

 final Drawable drawable = circle.getDrawable();
 final Animatable animatable = (Animatable) drawable;

 AVDWrapper.Callback callback = new AVDWrapper.Callback() {
@Override
public void onAnimationDone() {
tick.setAlpha(1f);
}

@Override
public void onAnimationStopped() {
// Okay
}
};

 AVDWrapper avdw = new AVDWrapper(animatable, mHandler, callback);
 //animatable.start();
 avdw.start(2000L);

 tick.setAlpha(0f);
 //tick.animate().alpha(1f).setStartDelay(2000).setDuration(1).start();

 // One wrapper is sufficient if the duration is same
 final Drawable drawable2 = tick.getDrawable();
 final Animatable animatable2 = (Animatable) drawable2;
 animatable2.start();
 But, this is exactly what you are doing with setStartDelay. So I don't know how useful this will be.

 Edit: All this can also be implemented inside an extended AnimatedVectorDrawable. But, you'll lose xml support altogether.
 **/