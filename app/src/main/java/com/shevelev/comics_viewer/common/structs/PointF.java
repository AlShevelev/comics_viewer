package com.shevelev.comics_viewer.common.structs;

public class PointF extends FloatStructBase
{
    public float left;
    public float top;

    public PointF(float left, float top)
    {
        this.left = left;
        this.top = top;
    }

    public Point toPoint(Size intSize)
    {
        return new Point(floatToInt(left, intSize.getWidth()),floatToInt(top, intSize.getHeight()));
    }
}
