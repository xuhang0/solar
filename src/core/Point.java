package core;

import java.awt.Color;

/**
 * 描特定的点，可以当作矢量
 * @author future
 *
 */
public class Point {
	private double x;
	private double y;
	private Color color;
	
	public Point(double x, double y, Color color) {
		super();
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
}
