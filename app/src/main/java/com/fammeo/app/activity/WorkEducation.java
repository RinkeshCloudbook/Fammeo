package com.fammeo.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fammeo.app.R;
import com.fammeo.app.animation.ViewAnimation;

public class WorkEducation extends AppCompatActivity {

    LinearLayout lyt_expand_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_education);

        lyt_expand_text = findViewById(R.id.lyt_expand_text);

        ((ImageButton) findViewById(R.id.bt_toggle_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSectionText(v);
            }
        });

        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void toggleSectionText(View v) {
        boolean show = toggleArrow(v);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    //Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }

    private boolean toggleArrow(View v) {
        if (v.getRotation() == 0) {
            v.animate().setDuration(200).rotation(180);
            return true;
        } else {
            v.animate().setDuration(200).rotation(0);
            return false;
        }
    }
}
