package ch.apprun.pixelmaler;

import android.graphics.Color;
import android.graphics.Paint;

public class Pixel {
    private int x;
    private int y;
    private String color;
    private Paint paint;

    public Pixel() {
    }

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pixel(int x, int y, Paint paint) {
        this.x = x;
        this.y = y;
        this.paint = paint;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
