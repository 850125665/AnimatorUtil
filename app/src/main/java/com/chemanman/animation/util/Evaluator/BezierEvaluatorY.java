package com.chemanman.animation.util.Evaluator;

import android.animation.TypeEvaluator;

/**
 * 改变曲线的y方向的值
 * Created by huilin on 2017/8/16.
 */

public class BezierEvaluatorY implements TypeEvaluator<Float> {

    @Override
    public Float evaluate(float fraction, Float startValue, Float endValue) {
        float oneMinusT = 1.0f - fraction;
        return oneMinusT * oneMinusT + startValue + 2 * oneMinusT * fraction * startValue + fraction * fraction * endValue;
    }
}
