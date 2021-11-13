package ch.apprun.pixelmaler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Die DrawingView ist für die Darstellung und Verwaltung der Zeichenfläche
 * zuständig.
 */
public class DrawingView extends View {

    private static final int GRID_SIZE = 13;
    private String color = "defaultColor";

    private Path drawPath = new Path();
    private Paint drawPaint = new Paint();
    private Paint linePaint = new Paint();

    private Paint bluePaint = new Paint();
    private Paint greenPaint = new Paint();
    private Paint yellowPaint = new Paint();
    private Paint redPaint = new Paint();
    private Paint blackPaint = new Paint();
    private Paint greyPaint = new Paint();
    private boolean isErasing = false;
    private int pixelWidth;
    private int pixelHeight;

    protected List<Pixel> pixels = new ArrayList<>();
    private List<TouchPoint> touchPoints = new ArrayList<>();

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        linePaint.setColor(0xFF666666);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1.0f);
        linePaint.setStyle(Paint.Style.STROKE);

        bluePaint.setStyle(Paint.Style.FILL);
        bluePaint.setColor(Color.rgb(19, 100, 183)); // #1364B7

        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setColor(Color.rgb(19, 183, 23)); // #13b717

        yellowPaint.setStyle(Paint.Style.FILL);
        yellowPaint.setColor(Color.rgb(255, 234, 0)); // #FFEA00

        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setColor(Color.rgb(217, 5, 5));// #D90505

        blackPaint.setStyle(Paint.Style.FILL);
        blackPaint.setColor(Color.rgb(0, 0, 0));// #000000

        greyPaint.setStyle(Paint.Style.FILL);
        greyPaint.setColor(Color.rgb(183, 183, 183));// #B7B7B7
    }

    @Override
    protected void onDraw(Canvas canvas) {

        final int maxX = getWidth();
        final int maxY = getHeight();

        pixelWidth = (int) Math.ceil((double) maxX / GRID_SIZE);
        pixelHeight = (int) Math.ceil((double) maxY / GRID_SIZE);

        drawGrid(canvas);

        drawRects(canvas);

        // Zeichnet einen Pfad der dem Finger folgt
        canvas.drawPath(drawPath, drawPaint);
    }

    private void drawGrid(Canvas canvas) {
        for (int i = 0; i < GRID_SIZE; i++) {
            canvas.drawLine(i*pixelWidth, 0, i*pixelWidth, GRID_SIZE*pixelHeight, linePaint);
        }

        for (int i = 0; i < GRID_SIZE; i++) {
            canvas.drawLine(0, i*pixelHeight, GRID_SIZE*pixelWidth, i*pixelHeight, linePaint);
        }
    }

    private void drawRects(Canvas canvas) {
        for (Pixel pixel : pixels) {
            float left = pixel.getX()*pixelWidth;
            float top = pixel.getY()*pixelHeight;
            float right = left+pixelWidth;
            float bottom = top+pixelHeight;
            RectF rectF = new RectF(left, top, right, bottom);
            canvas.drawRect(rectF, pixel.getPaint());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                touchPoints.add(new TouchPoint(touchX, touchY));

                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                touchPoints.add(new TouchPoint(touchX, touchY));

                break;
            case MotionEvent.ACTION_UP:

                // Jetzt können wir die zwischengespeicherten Punkte auf das
                // Gitter umrechnen und zeichnen, bzw. löschen, falls wir isErasing
                // true ist (optional)
                for (TouchPoint point : touchPoints) {
                    Pixel pixel = calculatePixel(point.getX(), point.getY());
                    pixel.setColor(color);
                    if (!isErasing) {
                        switch (color) {
                            case "#FF1364b7":
                                pixel.setPaint(bluePaint);
                                break;
                            case "#ff13b717":
                                pixel.setPaint(greenPaint);
                                break;
                            case "#FFffea00":
                                pixel.setPaint(yellowPaint);
                                break;
                            case "#FFD90505":
                                pixel.setPaint(redPaint);
                                break;
                            case "#FF000000":
                                pixel.setPaint(blackPaint);
                                break;
                            case "#FFB7B7B7":
                                pixel.setPaint(greyPaint);
                                break;
                            default:

                        }
                        setPixel(pixel);
                    } else {
                        removePixel(pixel.getX(), pixel.getY());
                    }
                }
                touchPoints.clear();

                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    private void setPixel(Pixel pixel) {
        for (int i = 0; i < pixels.size(); i++) {
            Pixel p = pixels.get(i);
            if (p.getX() == pixel.getX() && p.getY() == pixel.getY()) {
                pixels.set(i, pixel);
                return;
            }
        }
        pixels.add(pixel);
    }

    private void removePixel(int x, int y) {
        for (int i = 0; i < pixels.size(); i++) {
            if (pixels.get(i).getX() == x && pixels.get(i).getY() == y) {
                pixels.remove(i);
                break;
            }
        }
    }

    private Pixel calculatePixel(float touchX, float touchY) {
        int x = (int) touchX / pixelWidth;
        int y = (int) touchY / pixelHeight;
        return new Pixel(x, y);
    }

    public List<Pixel> getPixels() {
        return pixels;
    }

    public void startNew() {
        pixels.clear();
        touchPoints.clear();
        drawPath.reset();
    }

    public void setErase(boolean isErase) {
        isErasing = isErase;
        if (isErasing) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            drawPaint.setXfermode(null);
        }
    }

    public boolean isErasing() {
        return isErasing;
    }

    public void setColor(String color) {
//        invalidate();
        this.color = color;
//        drawPaint.setColor(Color.parseColor(color));
    }
}
