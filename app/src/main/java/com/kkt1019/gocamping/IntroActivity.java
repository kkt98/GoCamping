package com.kkt1019.gocamping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {

    ImageView iv;

    Animation ani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        iv= findViewById(R.id.iv);

        //애니메이션 객체 생성 - xml에 정의한 애니메이션의 설정값을 기반으로..
        ani= AnimationUtils.loadAnimation(this, R.anim.appear_logo);
        iv.startAnimation(ani);

        //애니메이션의 동작에 대한 리스너
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent= new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
