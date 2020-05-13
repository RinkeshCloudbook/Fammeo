package com.fammeo.app.view.siv;

import android.content.Context;
import android.util.AttributeSet;

import com.fammeo.app.R;
import com.fammeo.app.view.siv.shader.ShaderHelper;
import com.fammeo.app.view.siv.shader.SvgShader;

public class PentagonImageView extends ShaderImageView {

    public PentagonImageView(Context context) {
        super(context);
    }

    public PentagonImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PentagonImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader(R.raw.imgview_pentagon);
    }
}
