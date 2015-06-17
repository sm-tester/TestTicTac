package com.sm.testtictac;

public class Point
{
  private int mX;
  private int mY;
  
  public Point()
  {
    this.mX = 0;
    this.mY = 0;
  }
  
  public Point(int paramInt1, int paramInt2)
  {
    this.mX = paramInt1;
    this.mY = paramInt2;
  }
  
  public int getX()
  {
    return this.mX;
  }
  
  public int getY()
  {
    return this.mY;
  }
  
  public void setX(int paramInt)
  {
    this.mX = paramInt;
  }
  
  public void setY(int paramInt)
  {
    this.mY = paramInt;
  }
  
  public String toString()
  {
    return "(" + this.mX + ", " + this.mY + ")";
  }
}