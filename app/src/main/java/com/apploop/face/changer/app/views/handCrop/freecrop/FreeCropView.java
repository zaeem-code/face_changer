package com.apploop.face.changer.app.views.handCrop.freecrop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.List;

public class FreeCropView extends View implements OnTouchListener {

    static Bitmap f1142a;
    public static List<Point> points;
    int f1144c;
    int f1145d;
    int f1146e = 2;
    int f1147f;
    int f1148g;
    boolean f1149h = false;
    boolean f1150i = false;
    boolean f1151j = true;
    int f1152k;
    int f1153l;
    LayoutParams f1154m;
    Context f1155n;
    Point f1156o = null;
    Point f1157p = null;
    Paint f1158q = new Paint();
    private ScaleGestureDetector f1159r;
    private float f1160s = 1.0f;
    private Paint f1161t;

    private class C1376a extends SimpleOnScaleGestureListener {
        private C1376a() {
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            f1160s = f1160s * scaleGestureDetector.getScaleFactor();
            f1160s = Math.max(0.1f, Math.min(f1160s, 5.0f));
            invalidate();
            return true;
        }
    }

    public static boolean isCropImage() {
        return true;
    }

    public FreeCropView(Context c, Bitmap bmp) {
        super(c);

        f1142a = bmp;
        f1153l = f1142a.getWidth();
        f1152k = f1142a.getHeight();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("img_width");
        stringBuilder.append(f1153l);
        stringBuilder.append("img_height");
        stringBuilder.append(f1152k);

        ((Activity) c).getResources().getDisplayMetrics();

        f1148g = f1142a.getWidth();
        f1147f = f1142a.getHeight();
        if (f1153l <= f1148g) {
            f1145d = f1148g - f1153l;
        }
        if (f1152k <= f1147f) {
            f1144c = f1147f - f1152k;
        }
        f1155n = c;
        setFocusable(true);
        setFocusableInTouchMode(true);
        f1161t = new Paint(1);
        f1161t.setStyle(Style.STROKE);
        f1161t.setPathEffect(new DashPathEffect(new float[]{10.0f, 20.0f}, 5.0f));
        f1161t.setStrokeWidth(5.0f);
        f1161t.setColor(-1);
        if (VERSION.SDK_INT >= 11) {
            setLayerType(1, f1161t);
        }
        f1161t.setShadowLayer(5.5f, 6.0f, 6.0f, Integer.MIN_VALUE);
        f1154m = new LayoutParams(f1142a.getWidth(), f1142a.getHeight());
        setOnTouchListener(this);
        points = new ArrayList();
        f1150i = false;
        f1159r = new ScaleGestureDetector(c, new C1376a());

    }


    private boolean m1535a(Point first, Point current) {
        int left_range_x = (int) (current.x - 3);
        int left_range_y = (int) (current.y - 3);

        int right_range_x = (int) (current.x + 3);
        int right_range_y = (int) (current.y + 3);

        if ((left_range_x < first.x && first.x < right_range_x)
                && (left_range_y < first.y && first.y < right_range_y)) {
            if (points.size() < 10) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    public boolean getBooleanValue() {
        return f1149h;
    }


    public void onDraw(Canvas canvas) {
        canvas.scale(f1160s, f1160s);
        canvas.drawBitmap(f1142a, 0.0f, 0.0f, null);
        Path path = new Path();
        Integer valueOf = Integer.valueOf(1);
        for (int i = 0; i < points.size(); i += 2) {
            Point point = (Point) points.get(i);
            if (valueOf != null) {
                path.moveTo((float) point.x, (float) point.y);
                valueOf = null;
            } else if (i < points.size() - 1) {
                Point point2 = (Point) points.get(i + 1);
                path.quadTo((float) point.x, (float) point.y, (float) point2.x, (float) point2.y);
            } else {
                f1157p = (Point) points.get(i);
                path.lineTo((float) point.x, (float) point.y);
            }
        }
        canvas.drawPath(path, f1161t);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        Point point = new Point();
        point.x = (int) motionEvent.getX();
        point.y = (int) motionEvent.getY();
        if (f1151j) {
            if (f1150i) {
                if (m1535a(f1156o, point)) {
                    points.add(f1156o);
                    f1151j = false;
                    isCropImage();
                } else if (point.x <= f1153l && point.y <= f1152k) {
                    points.add(point);
                }
            } else if (point.x <= f1153l && point.y <= f1152k) {
                points.add(point);
            }
            if (!f1150i) {
                f1156o = point;
                f1150i = true;
            }
        } else {
            f1159r.onTouchEvent(motionEvent);
        }
        invalidate();
        if (motionEvent.getAction() == 1) {
            f1157p = point;
            if (f1151j && points.size() > 12 && m1535a(f1156o, f1157p)) {
                f1151j = false;
                points.add(f1156o);
                isCropImage();
            }
        }
        return true;
    }
}
