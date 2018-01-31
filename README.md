# AnimatorUtil
+ 我选择属性动画。补间动画虽能对控件做动画，但并没有改变控件内部的属性值。而属性动画则是恰恰相反，属性动画是通过改变控件内部的属性值来达到动画效果的。
+	ObjectAnimator比ValueAnimator多了一个setTarget的方法，ObjectAnimator直接使用要修改属性值的对象，不像ValueAnimator是通过监听数值变化，再做相应的处理。在代码简洁性上来说，ObjectAnimator比ValueAnimator更好。所有选用ObjectAnimator来实现动画效果。
+ 要实现组合动画，所以动画类构造出来的对象是AnimatorSet通过AnimatorSet.Builder 来拼接动画，但因为AnimatorSet.Builder 只能通过animatorSet.play(animator)生成，所以Builder类的构造方法必须传个animator。
+ 然后通过with ，befor，after等方法来组合动画，期实现是通过AnimatorSet.Builder 的with，befor，after方法。所以由上面构造方法必须得生成一个AnimatorSet.Builder。
+ 因为大多动画用ObjectAnimator ofFloat(Object target, String propertyName, float... values)就可以实现，所以拼接与构造的默认动画是用ofFloat生成的。无需调用者实现。
+ 特殊的动画，如曲线动画我们得实现自定义估值器来计算每个进度的点。主要通过ObjectAnimator. ofObject(Object target, String propertyName,TypeEvaluator evaluator, Object... values)实现。需要由调用者在外部实现传递。
具体使用及原理见 [http://note.youdao.com/noteshare?id=4ff1de4387ba74dd946a1c1f47ef75ca&sub=DED2554D573149B096721E4D094AF984](http://note.youdao.com/noteshare?id=4ff1de4387ba74dd946a1c1f47ef75ca&sub=DED2554D573149B096721E4D094AF984)
