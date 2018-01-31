package com.chemanman.animation.view;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chemanman.animation.R;
import com.chemanman.animation.util.MMAnimator;

/**
 * Created by huilin on 2017/8/16.
 */
public class MainActivity extends AppCompatActivity {

    private TextView mTvLeft;
    private TextView mTvRight;
    private ImageView mIvCenter;
    private Button mBtOne;
    private float oneTranslate;
    private boolean swap = false;
    private ImageView mIvCurveTop;
    private ImageView mIvCurveBottom;
    private ImageView mIvCurveItem;
    private RelativeLayout mRlCurveGroup;
    private float curveStartX;
    private float curveStartY;
    private float curveEndX;
    private float curveEndY;
    private MMAnimator mmAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvLeft = (TextView) findViewById(R.id.tv_left);
        mTvRight = (TextView) findViewById(R.id.tv_right);
        mIvCenter = (ImageView) findViewById(R.id.iv_center);
        mBtOne = (Button) findViewById(R.id.bt_one);

        mIvCurveBottom = (ImageView) findViewById(R.id.iv_curve_bottom);
        mIvCurveTop = (ImageView) findViewById(R.id.iv_curve);
        mRlCurveGroup = (RelativeLayout) findViewById(R.id.rl_curve);

        mTvLeft.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                curveStartX = mIvCurveTop.getX() + mIvCurveTop.getWidth() / 4;
                curveStartY = mIvCurveTop.getY() + mIvCurveTop.getHeight() / 4;

                curveEndX = mIvCurveBottom.getX();
                curveEndY = mIvCurveBottom.getY();
                oneTranslate = mTvRight.getX();
                mTvLeft.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mBtOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swap = !swap;
                //交互城市
                new MMAnimator.Builder(mTvLeft, MMAnimator.ANIM_TRANSLATE_X, swap ? 0 : oneTranslate, swap ? oneTranslate : 0)
                        .with(mTvRight, MMAnimator.ANIM_TRANSLATE_X, swap ? 0 : -oneTranslate, swap ? -oneTranslate : 0)
                        .with(mTvRight, MMAnimator.ANIM_ALPHA, 1.0f, 0.7f, 0.3f, 0.0f, 0.3f, 0.7f, 1.0f)
                        .with(mTvLeft, MMAnimator.ANIM_ALPHA, 1.0f, 0.7f, 0.3f, 0.0f, 0.3f, 0.7f, 1.0f)
                        .with(mIvCenter, MMAnimator.ANIM_ROTATE, 0, 360)
                        .setDuration(300)
                        .build()
                        .start();
            }
        });


        mIvCurveItem = new ImageView(this);
        mIvCurveItem.setBackgroundResource(R.mipmap.xianjin);

        mIvCurveTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mmAnimator == null) {
                    mmAnimator = MMAnimator.curveAnimatorBuilder(mIvCurveItem, curveStartX,
                            curveStartY, curveEndX, curveEndY, new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    mRlCurveGroup.addView(mIvCurveItem,
                                            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mRlCurveGroup.removeView(mIvCurveItem);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    mRlCurveGroup.removeView(mIvCurveItem);
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            })
                            .before(MMAnimator.pendulumAnimatorBuilder(mIvCurveBottom, null).build().getAnimatorSet())
//                            .setInterpolator(new BounceInterpolator())
                            .build();
                }
                if (mmAnimator.isRunning()) {
                    mmAnimator.cancel();
                    mmAnimator.start();
                } else {
                    mmAnimator.start();
                }
            }
        });

    }


}
