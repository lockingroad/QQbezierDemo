# QQ黏性贝塞尔曲线

## 原理
* 判断触摸到图片
* 黏性效果通过贝塞尔曲线实现
* 触摸监听找到4个关键点


## 实现

### 判断触摸到图片
通过img的坐标和motionevent的坐标确定是否触摸了图片
```android
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
}
```
### 获取关键点

![qq_bezier.png](http://upload-images.jianshu.io/upload_images/2482523-05f08aa2633715aa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

先在监听事件中触摸点的坐标，通过触摸点的坐标计算各点坐标 然后重画
```android

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

```
计算过程
```android
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
```
