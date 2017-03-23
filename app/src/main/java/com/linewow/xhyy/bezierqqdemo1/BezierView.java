package com.linewow.xhyy.bezierqqdemo1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
/**
 * Created by LXR on 2017/3/14.
 * 1.init中得不到 img的半径
 * 2. canvas中方法的应用 drawcolor path
 * 3.用rect判断是否在触摸区域下
 */

public class BezierView extends FrameLayout {
    private Context context;
    private int nowX,nowY;
    private int anchorX,anchorY;
    private int radius=60;
    private int startX=100;
    private int startY=100;

    private float slope;
    private Paint paint;
    private ImageView moveImg;
    private boolean isTouch;
    private int imgHei,imgWid;

    private Path path;
    private String TAG="BezierView";


    public BezierView(Context context) {
        this(context,null);

    }

    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }



    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }
    private void init(Context context) {
        this.context=context;
        path=new Path();
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);
        LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        moveImg=new ImageView(context);
        moveImg.setImageResource(R.mipmap.skin_tips_newmessage_ninetynine);
        moveImg.setLayoutParams(lp);
        addView(moveImg);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        imgWid=moveImg.getWidth();
        imgHei=moveImg.getHeight();
        moveImg.setX(startX-imgWid/2);
        moveImg.setY(startY-imgHei/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isTouch){
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
            canvas.drawCircle(startX,startY,radius,paint);
        }else{
            calculate();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.OVERLAY);
            canvas.drawPath(path,paint);
            canvas.drawCircle(startX,startY,radius,paint);
            canvas.drawCircle(nowX,nowY,radius,paint);
        }
        super.onDraw(canvas);
    }

    public void calculate(){
        path.reset();
        int offsetY= (int) (radius*Math.sin(Math.atan(slope)));
        int offsetX= (int) (radius*Math.cos(Math.atan(slope)));
        int x1=startX+offsetX;
        int y1=startY-offsetY;
        int x2=nowX+offsetX;
        int y2=nowY-offsetY;
        int x3=nowX-offsetX;
        int y3=nowY+offsetY;
        int x4=startX-offsetX;
        int y4=startY+offsetY;

        path.moveTo(x1,y1);
        path.quadTo(anchorX,anchorY,x2,y2);
        path.lineTo(x3,y3);
        path.quadTo(anchorX,anchorY,x4,y4);
        path.lineTo(x1,y1);
        Log.e(TAG,"x4:"+x4+"---y4:"+y4);
        Log.e(TAG,"计算过程中x"+offsetX+"---y"+offsetY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_DOWN){
            int[]location=new int[2];
            Rect rect=new Rect();
            moveImg.getLocationOnScreen(location);
            moveImg.getDrawingRect(rect);
            rect.left=location[0];
            rect.top=location[1];
            rect.right=location[0]+rect.right;
            rect.bottom=location[1]+rect.bottom;
            if(rect.contains((int) event.getRawX(),(int) event.getRawY())){
                isTouch=true;
            }
        }else if(event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL){
            moveImg.setX(startX-imgWid/2);
            moveImg.setY(startY-imgHei/2);
            isTouch=false;
        }
        nowX= (int) event.getX();
        nowY= (int) event.getY();
        anchorX=(startX+nowX)/2;
        anchorY=(startY+nowY)/2;
        slope=((float)(nowY-startY))/((float) (nowX-startX));
        slope=1/slope;//互余
        if(isTouch){
            moveImg.setX(event.getX()-imgWid/2);
            moveImg.setY(event.getY()-imgHei/2);
        }
        invalidate();
        return true;
    }
}
