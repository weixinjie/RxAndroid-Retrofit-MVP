package com.will.custom_rxandroid.ui.drawable_test;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.will.custom_rxandroid.R;
import com.will.custom_rxandroid.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/*
* 参考资源:
* http://blog.csdn.net/ouyang_peng/article/details/8800743
* http://keeganlee.me/post/android/20150916
**/

public class DrawableActivity extends AppCompatActivity {

    @BindView(R.id.iv_clip)
    ImageView iv_clip;
    @BindView(R.id.iv_scale)
    ImageView iv_scale;
    @BindView(R.id.iv_rotate)
    ImageView iv_rotate;
    @BindView(R.id.iv_animation_list)
    ImageView iv_animation_list;
    @BindView(R.id.iv_level_list)
    ImageView iv_level_list;
    @BindView(R.id.iv_transition)
    ImageView iv_transition;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        ButterKnife.bind(this);
        final ClipDrawable clipDrawable = (ClipDrawable) iv_clip.getDrawable();
        final ScaleDrawable scaleDrawable = (ScaleDrawable) iv_scale.getDrawable();
        final RotateDrawable rotateDrawable = (RotateDrawable) iv_rotate.getDrawable();
        Observable.range(0, 10000).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer aLong) {
                        clipDrawable.setLevel(Integer.parseInt(String.valueOf(aLong)));
                        scaleDrawable.setLevel(Integer.parseInt(String.valueOf(aLong)));
                        rotateDrawable.setLevel(aLong);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_animation_list.getDrawable();
        animationDrawable.start();

        LevelListDrawable levelListDrawable = (LevelListDrawable) iv_level_list.getDrawable();
        levelListDrawable.setLevel(5000);

        //transition标签生成的Drawable对应的类为TransitionDrawable，要切换时，需要主动调用TransitionDrawable的startTransition()方法，
        // 参数为动画的毫秒数，也可以调用reverseTransition()方法逆向切换。
        //((TransitionDrawable)drawable).startTransition(500); //正向切换，即从第一个drawable切换到第二个
        //((TransitionDrawable)drawable).reverseTransition(500); //逆向切换，即从第二个drawable切换回第一个
        TransitionDrawable transitionDrawabl = (TransitionDrawable) iv_transition.getDrawable();
        transitionDrawabl.reverseTransition(4000);
    }
}
