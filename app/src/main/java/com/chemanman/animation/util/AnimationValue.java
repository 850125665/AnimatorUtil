package com.chemanman.animation.util;

/**
 * 存放通用动画属性变化枚举值
 * Created by huilin on 2017/8/17.
 */
interface AnimationValue {

    /**
     * 钟摆动画水平方向变化枚举值
     */
    float[] PENDULUM_TRANSLATE_X_VALUE = new float[]{
            0, 25, -25, 25, -25, 15, -15, 6, -6, 0
    };

    /**
     * 钟摆动画放大变化枚举值
     */
    float[] PENDULUM_SCALE_VALUE = new float[]{
            1f, 1.2f, 1f
    };
}
