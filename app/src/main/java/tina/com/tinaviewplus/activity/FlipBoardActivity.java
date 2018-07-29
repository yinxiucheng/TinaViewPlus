package tina.com.tinaviewplus.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import tina.com.tinaviewplus.R;
import tina.com.tinaviewplus.view.MapView;

/**
 * @author yxc
 * @date 2018/7/29
 */
public class FlipBoardActivity extends AppCompatActivity{
    private Handler handler = new Handler();
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_board);
        initView();
    }

    /**
     * 整个动画被拆分成为三个部分
     * 1、绕Y轴3D旋转45度
     * 2、绕Z轴3D旋转270度
     * 3、不变的那一半（上半部分）绕Y轴旋转30度（注意，这里canvas已经旋转了270度，计算第三个动效参数时要注意）
     */
    private void initView() {
        mapView =  findViewById(R.id.map_layout);
        mapView.setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.flip_board));

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mapView, "degreeY", 0, -60);
        animator1.setDuration(1000);
        animator1.setStartDelay(500);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mapView, "degreeZ", 0, 270);
        animator2.setDuration(800);
        animator2.setStartDelay(500);


        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mapView, "fixDegreeY", 0, 30);
        animator3.setDuration(800);
        animator3.setStartDelay(500);
        animator3.setRepeatCount(1);
        animator3.setRepeatMode(ValueAnimator.REVERSE);

        final AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mapView.reset();
                                set.start();
                            }
                        });
                    }
                }, 500);
            }
        });
        set.playSequentially(animator1, animator2, animator3);
        set.start();
        animator1.reverse();
//        animator2.start();
    }
}
