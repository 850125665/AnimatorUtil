package com.chemanman.animation.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.chemanman.animation.util.Evaluator.BezierEvaluatorX;
import com.chemanman.animation.util.Evaluator.BezierEvaluatorY;


/**
 * 说明 链式调用android动画，单个，多个动画组装
 * <p>
 * 用法 MMAnimator.Builder(target, MMAnimator.ANIM_TRANSLATE_X, AnimationValue.PENDULUM_TRANSLATE_X_VALUE)
 * .with(target, MMAnimator.ANIM_SCALE_X, AnimationValue.PENDULUM_SCALE_VALUE) .with(target,
 * MMAnimator.ANIM_SCALE_Y, AnimationValue.PENDULUM_SCALE_VALUE) .setInterpolator(new
 * AccelerateInterpolator()) .build() .start()
 * <p>
 * 提供默认方法如下： 钟摆动画 MMAnimator.pendulumAnimatorBuilder(mIvCurveBottom, null) .build() .start()
 * <p>
 * 曲线动画 MMAnimator.curveAnimatorBuilder(mIvCurveItem, curveStartX, curveStartY, curveEndX,
 * curveEndY, animatorListener) .build() .start()
 * <p>
 * <p>
 * Created by huilin on 2017/8/17.
 */

public final class MMAnimator {

    /**
     * 默认动画：钟摆动画
     *
     * @param target 动画view
     * @param animatorListener 钟摆动画监听器
     *
     * @return MMAnimator.Builder
     */
    public static MMAnimator.Builder pendulumAnimatorBuilder(View target, @Nullable Animator.AnimatorListener animatorListener) {
        return new MMAnimator.Builder(target,
                                      MMAnimator.ANIM_TRANSLATE_X,
                                      400,
                                      new BounceInterpolator(),
                                      animatorListener,
                                      AnimationValue.PENDULUM_TRANSLATE_X_VALUE).with(target,
                                                                                      MMAnimator.ANIM_SCALE_X,
                                                                                      400,
                                                                                      new BounceInterpolator(),
                                                                                      null,
                                                                                      AnimationValue.PENDULUM_SCALE_VALUE)
                                                                                .with(target,
                                                                                      MMAnimator.ANIM_SCALE_Y,
                                                                                      400,
                                                                                      new BounceInterpolator(),
                                                                                      null,
                                                                                      AnimationValue.PENDULUM_SCALE_VALUE);
    }

    /**
     * 默认动画：曲线动画
     *
     * @param container target容器
     * @param targetRes 目标视图资源
     * @param start 起始参考位置
     * @param end 结束参考位置
     *
     * @return MMAnimator.Builder
     */
    public static MMAnimator.Builder curveAnimatorBuilder(final ViewGroup container, @DrawableRes int targetRes, View start, View end, @Nullable final Animator.AnimatorListener animatorListener) {
        final ImageView iv = new ImageView(container.getContext());
        iv.setImageResource(targetRes);

        return curveAnimatorBuilder(container, iv, start, end, animatorListener);
    }

    /**
     * 默认动画：曲线动画
     *
     * @param container target容器
     * @param target 目标视图
     * @param start 起始参考位置
     * @param end 结束参考位置
     * @param animatorListener 曲线动画监听器
     *
     * @return MMAnimator.Builder
     */
    public static MMAnimator.Builder curveAnimatorBuilder(final ViewGroup container, View target, View start, View end, @Nullable final Animator.AnimatorListener animatorListener) {
        final ImageView iv = new ImageView(container.getContext());
        target.setDrawingCacheEnabled(true);
        iv.setImageBitmap(target.getDrawingCache());

        return curveAnimatorBuilder(container, iv, start, end, animatorListener);
    }

    /**
     * 曲线动画 new一个无约束的ImageView作为动画，禁止外部调用
     */
    private static MMAnimator.Builder curveAnimatorBuilder(final ViewGroup container, final ImageView freeIV, View start, View end, @Nullable final Animator.AnimatorListener animatorListener) {
        int[] startXY = new int[2];
        int[] endXY = new int[2];

        start.getLocationInWindow(startXY);
        end.getLocationInWindow(endXY);
        endXY[0] += end.getWidth() / 2f;
        endXY[1] += end.getHeight() / 2f;

        return curveAnimatorBuilder(freeIV,
                                    startXY[0],
                                    startXY[1],
                                    endXY[0],
                                    endXY[1],
                                    new Animator.AnimatorListener() {

                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            container.addView(freeIV,
                                                              new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                                                         ViewGroup.LayoutParams.WRAP_CONTENT));
                                            if (animatorListener != null) {
                                                animatorListener.onAnimationStart(animation);
                                            }
                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            container.removeView(freeIV);
                                            freeIV.setImageBitmap(null);
                                            if (animatorListener != null) {
                                                animatorListener.onAnimationEnd(animation);
                                            }
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {
                                            container.removeView(freeIV);
                                            freeIV.setImageBitmap(null);
                                            if (animatorListener != null) {
                                                animatorListener.onAnimationCancel(animation);
                                            }
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {
                                            if (animatorListener != null) {
                                                animatorListener.onAnimationRepeat(animation);
                                            }
                                        }
                                    });
    }

    /**
     * 默认动画：曲线动画
     *
     * @param target 动画view
     * @param startX 起始点x坐标
     * @param startY 起始点y坐标
     * @param endX 结束点x坐标
     * @param endY 结束点y坐标
     * @param animatorListener 曲线动画监听器
     *
     * @return MMAnimator.Builder
     */
    public static MMAnimator.Builder curveAnimatorBuilder(View target, float startX, float startY, float endX, float endY, @Nullable Animator.AnimatorListener animatorListener) {
        return new MMAnimator.Builder(ObjectAnimator.ofObject(target,
                                                              MMAnimator.ANIM_TRANSLATE_X,
                                                              new BezierEvaluatorX(),
                                                              startX,
                                                              endX), 600, new LinearInterpolator(), null).with(
                ObjectAnimator.ofObject(target, MMAnimator.ANIM_TRANSLATE_Y, new BezierEvaluatorY(), startY, endY),
                600,
                new LinearInterpolator(),
                animatorListener);
    }

    /**
     * 底部进出动画
     *
     * @param target 目标视图
     * @param action 进出区分
     * @param builderListener 侦听器
     */
    public static void buildBottomTransAnim(final View target, @AnimationAction final int action, @NonNull final MMAnimatorBuilderListener builderListener) {
        target.post(new Runnable() {

            @Override
            public void run() {
                int[] targetXY = new int[2];
                target.getLocationInWindow(targetXY);
                int startX = action == ANIM_IN ? targetXY[1] : 0;
                int endX = action == ANIM_IN ? 0 : targetXY[1];
                Builder builder = new Builder(target, MMAnimator.ANIM_TRANSLATE_Y, 400, null, null, startX, endX);
                builderListener.onAnimatorBuild(builder);
            }
        });
    }

    /**
     * 右侧进出动画
     *
     * @param target 目标视图
     * @param action 进出区分
     * @param builderListener 侦听器
     */
    public static void buildRightTransAnim(final View target, @AnimationAction final int action, @NonNull final MMAnimatorBuilderListener builderListener) {
        target.post(new Runnable() {

            @Override
            public void run() {
                int[] targetXY = new int[2];
                target.getLocationInWindow(targetXY);
                int startX = action == ANIM_IN ? targetXY[0] : 0;
                int endX = action == ANIM_IN ? 0 : targetXY[0];
                Builder builder = new Builder(target, MMAnimator.ANIM_TRANSLATE_X, 400, null, null, startX, endX);
                builderListener.onAnimatorBuild(builder);
            }
        });
    }

    /**
     * 获取MMAnimator.Builder builder监听器
     */
    public interface MMAnimatorBuilderListener {

        void onAnimatorBuild(MMAnimator.Builder builder);
    }


    public static final String ANIM_TRANSLATE_X = "translationX";
    public static final String ANIM_TRANSLATE_Y = "translationY";
    public static final String ANIM_TRANSLATE_Z = "translationZ";
    public static final String ANIM_ROTATE = "rotation";
    public static final String ANIM_ROTATE_X = "rotationX";
    public static final String ANIM_ROTATE_Y = "rotationY";
    public static final String ANIM_SCALE_X = "scaleX";
    public static final String ANIM_SCALE_Y = "scaleY";
    public static final String ANIM_ALPHA = "alpha";
    public static final String ANIM_X = "X";
    public static final String ANIM_Y = "Y";


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ANIM_TRANSLATE_X,
                ANIM_TRANSLATE_Y,
                ANIM_TRANSLATE_Z,
                ANIM_ALPHA,
                ANIM_ROTATE,
                ANIM_ROTATE_X,
                ANIM_ROTATE_Y,
                ANIM_SCALE_X,
                ANIM_SCALE_Y,
                ANIM_X,
                ANIM_Y})
    @interface PropertyName {}


    public static final int ANIM_IN = 0;  // 进入动作
    public static final int ANIM_OUT = 1;  // 退出动作


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ANIM_IN, ANIM_OUT})
    @interface AnimationAction {}


    private AnimatorSet mAnimatorSet;

    private MMAnimator(Builder builder) {
        mAnimatorSet = builder.mAnimatorSet;
    }

    /**
     * 执行动画
     */
    public void start() {
        mAnimatorSet.start();
    }

    /**
     * 判断动画是否在运行
     *
     * @return
     */
    public boolean isRunning() {
        return mAnimatorSet.isRunning();
    }

    /**
     * 取消动画
     */
    public void cancel() {
        mAnimatorSet.cancel();
    }

    /**
     * 移除动画监听器
     *
     * @param listener 动画监听器
     */
    public void removeListener(Animator.AnimatorListener listener) {
        mAnimatorSet.removeListener(listener);
    }

    /**
     * 移除所有动画监听器
     */
    public void removeAllListener() {
        mAnimatorSet.removeAllListeners();
    }

    /**
     * 获取当前构建动画实例
     *
     * @return 动画实例
     */
    public AnimatorSet getAnimatorSet() {
        return mAnimatorSet;
    }

    /**
     * 默认使用ObjectAnimator.ofFloat(view, propertyName, values)构建动画
     * 使用其他方法构建动画传入构建好的对象即可
     */
    public static class Builder {

        private AnimatorSet mAnimatorSet;
        private AnimatorSet.Builder mAnimatorSetBuilder;

        /**
         * @param animator 属性动画
         * @param duration 动画执行时间
         * @param timeInterpolator 插值器
         * @param animatorListener 动画监听器
         */
        private void buildAnimator(Animator animator, long duration, @Nullable TimeInterpolator timeInterpolator, @Nullable Animator.AnimatorListener animatorListener) {
            if (duration > 0) {
                animator.setDuration(duration);
            }
            if (timeInterpolator != null) {
                animator.setInterpolator(timeInterpolator);
            }
            if (animatorListener != null) {
                animator.addListener(animatorListener);
            }
        }

        /**
         * 构建ObjectAnimator.ofFloat动画
         *
         * @param view 执行动画的view
         * @param propertyName 改变的属性name
         * @param values 动画执行时间内所改变的一组值
         */
        public Builder(View view, @PropertyName String propertyName, float... values) {
            this(view, propertyName, 0, null, null, values);
        }

        /**
         * @param animator 属性动画
         */
        public Builder(Animator animator) {
            this(animator, 0, null, null);
        }

        /**
         * 构建ObjectAnimator.ofFloat动画
         *
         * @param view 执行懂的view
         * @param propertyName 改变属性的name
         * @param duration 执行动画的时间
         * @param timeInterpolator 插值器
         * @param animatorListener 动画监听器
         * @param values 动画执行时间内所改变的一组值
         */
        public Builder(View view, @PropertyName String propertyName, long duration, @Nullable TimeInterpolator timeInterpolator, @Nullable Animator.AnimatorListener animatorListener, float... values) {
            this(ObjectAnimator.ofFloat(view, propertyName, values), duration, timeInterpolator, animatorListener);
        }

        /**
         * @param animator 属性动画
         * @param duration 执行动画时间
         * @param timeInterpolator 插值器
         * @param animatorListener 动画监听器
         */
        public Builder(Animator animator, long duration, @Nullable TimeInterpolator timeInterpolator, @Nullable Animator.AnimatorListener animatorListener) {
            mAnimatorSet = new AnimatorSet();
            buildAnimator(animator, duration, timeInterpolator, animatorListener);
            mAnimatorSetBuilder = mAnimatorSet.play(animator);
        }

        /**
         * 和前面动画一起执行
         * 构建ObjectAnimator.ofFloat动画
         *
         * @param view 执行动画的view
         * @param propertyName 改变的属性
         * @param values 动画执行时间内所改变的一组值
         */
        public Builder with(View view, @PropertyName String propertyName, float... values) {
            return with(view, propertyName, 0, null, null, values);
        }

        /**
         * 和前面动画一起执行
         *
         * @param animator 属性动画
         */
        public Builder with(Animator animator) {
            return with(animator, 0, null, null);
        }

        /**
         * 和前面动画一起执行
         * 构建ObjectAnimator.ofFloat动画
         *
         * @param view 执行动画的view
         * @param propertyName 改变的属性name
         * @param duration 动画执行时间
         * @param timeInterpolator 插值器
         * @param animatorListener 动画监听器
         * @param values 动画执行时间内所改变的一组值
         */
        public Builder with(View view, @PropertyName String propertyName, long duration, @Nullable TimeInterpolator timeInterpolator, @Nullable Animator.AnimatorListener animatorListener, float... values) {
            return with(ObjectAnimator.ofFloat(view, propertyName, values),
                        duration,
                        timeInterpolator,
                        animatorListener);
        }

        /**
         * 和前面动画一起执行
         *
         * @param animator 属性动画
         * @param duration 执行动画时间
         * @param timeInterpolator 插值器
         * @param animatorListener 动画监听器
         */
        public Builder with(Animator animator, long duration, @Nullable TimeInterpolator timeInterpolator, @Nullable Animator.AnimatorListener animatorListener) {
            buildAnimator(animator, duration, timeInterpolator, animatorListener);
            mAnimatorSetBuilder.with(animator);
            return this;
        }

        /**
         * 执行完前面的动画后才执行该动画
         * 构建ObjectAnimator.ofFloat动画
         *
         * @param view 执行动画的view
         * @param propertyName 改变的属性name
         * @param values 动画执行时间内所改变的一组值
         */
        public Builder before(View view, @PropertyName String propertyName, float... values) {
            return before(view, propertyName, 0, null, null, values);
        }

        /**
         * 执行完前面的动画后才执行该动画
         *
         * @param animator 属性动画
         */
        public Builder before(Animator animator) {
            return before(animator, 0, null, null);
        }

        /**
         * 执行完前面的动画后才执行该动画
         * 构建ObjectAnimator.ofFloat动画
         *
         * @param view 执行动画的view
         * @param propertyName 改变的属性bane
         * @param duration 动画执行时间
         * @param timeInterpolator 插值器
         * @param animatorListener 动画监听器
         * @param values 动画执行时间内所改变的一组值
         */
        public Builder before(View view, @PropertyName String propertyName, long duration, @Nullable TimeInterpolator timeInterpolator, @Nullable Animator.AnimatorListener animatorListener, float... values) {
            return before(ObjectAnimator.ofFloat(view, propertyName, values),
                          duration,
                          timeInterpolator,
                          animatorListener);
        }

        /**
         * 执行完前面的动画后才执行该动画
         *
         * @param animator 属性动画
         * @param duration 执行动画时间
         * @param timeInterpolator 插值器
         * @param animatorListener 动画监听器
         */
        public Builder before(Animator animator, long duration, @Nullable TimeInterpolator timeInterpolator, @Nullable Animator.AnimatorListener animatorListener) {
            buildAnimator(animator, duration, timeInterpolator, animatorListener);
            mAnimatorSetBuilder.before(animator);
            return this;
        }

        /**
         * 先执行该动画在执行前面的动画
         * 构建ObjectAnimator.ofFloat动画
         *
         * @param view 执行动画的view
         * @param propertyName 改变属性的name
         * @param values 动画执行时间内所改变的一组值
         */
        public Builder after(View view, @PropertyName String propertyName, float... values) {
            return after(view, propertyName, 0, null, null, values);
        }

        /**
         * 先执行该动画在执行前面的动画
         *
         * @param animator 属性动画
         */
        public Builder after(Animator animator) {
            return after(animator, 0, null, null);
        }

        /**
         * 先执行该动画在执行前面的动画
         * 构建ObjectAnimator.ofFloat动画
         *
         * @param view 执行动画的view
         * @param propertyName 改变属性的name
         * @param duration 执行动画的时间
         * @param timeInterpolator 插值器
         * @param animatorListener 动画监听器
         * @param values 动画执行时间内所改变的一组值
         */
        public Builder after(View view, @PropertyName String propertyName, long duration, @Nullable TimeInterpolator timeInterpolator, @Nullable Animator.AnimatorListener animatorListener, float... values) {
            return after(ObjectAnimator.ofFloat(view, propertyName, values),
                         duration,
                         timeInterpolator,
                         animatorListener);
        }

        /**
         * 先执行该动画在执行前面的动画
         *
         * @param animator 属性动画
         * @param duration 执行动画时间
         * @param timeInterpolator 插值器
         * @param animatorListener 动画监听器
         */
        public Builder after(Animator animator, long duration, @Nullable TimeInterpolator timeInterpolator, @Nullable Animator.AnimatorListener animatorListener) {
            buildAnimator(animator, duration, timeInterpolator, animatorListener);
            mAnimatorSetBuilder.after(animator);
            return this;
        }

        /**
         * 延迟 n毫秒之后执行动画
         *
         * @param delay 延迟的时间（毫秒）
         */
        public Builder after(long delay) {
            mAnimatorSetBuilder.after(delay);
            return this;
        }

        /**
         * 统一设置插值器（单独设置的插值器失效）
         *
         * @param interpolator 插值器
         */
        public Builder setInterpolator(TimeInterpolator interpolator) {
            if (interpolator != null) {
                mAnimatorSet.setInterpolator(interpolator);
            }
            return this;
        }

        /**
         * 统一设置时间（单独设置的时间失效）
         *
         * @param duration 执行时间（毫秒）
         */
        public Builder setDuration(long duration) {
            mAnimatorSet.setDuration(duration);
            return this;
        }

        /**
         * 添加整个动画的监听
         *
         * @param animatorListener 动画监听器
         */
        public Builder addListener(Animator.AnimatorListener animatorListener) {
            if (animatorListener != null) {
                mAnimatorSet.addListener(animatorListener);
            }
            return this;
        }

        /**
         * 构建MMAnimator
         *
         * @return MMAnimator类
         */
        public MMAnimator build() {
            return new MMAnimator(this);
        }
    }

}
