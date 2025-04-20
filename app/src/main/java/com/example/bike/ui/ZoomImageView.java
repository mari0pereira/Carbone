package com.example.bike.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import androidx.appcompat.widget.AppCompatImageView;

public class ZoomImageView extends AppCompatImageView {
    private static final float ZOOM_SCALE = 1.1f;
    private boolean isZoomed = false;

    public ZoomImageView(Context context) {
        super(context);
        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Quando o usuário toca na imagem
                if (!isZoomed) {
                    animate()
                            .scaleX(ZOOM_SCALE)
                            .scaleY(ZOOM_SCALE)
                            .setDuration(300)
                            .start();
                    isZoomed = true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Quando o usuário solta ou cancela o toque
                if (isZoomed) {
                    animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(300)
                            .start();
                    isZoomed = false;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}