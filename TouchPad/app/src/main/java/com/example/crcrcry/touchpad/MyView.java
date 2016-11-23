package com.example.crcrcry.touchpad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View{
    public float X = 50;
    public float Y = 50;

    //创建画笔
    Paint paint = new Paint();

    public MyView(Context context,AttributeSet set)
    {
        super(context,set);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(X,Y,30,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.X = event.getX();
        this.Y = event.getY();
        //通知组件进行重绘
        //this.invalidate();
        return true;
    }
}