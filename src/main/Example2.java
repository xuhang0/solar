package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import core.Calc;
import core.Earth;
import core.Point;
import core.Sun;

/**
 * 验证 KEPLER 定律
 * @author future
 *
 */
public class Example2 extends Example{
	/**
	 * Graphics of panel
	 */
	private MyPanel mp;
	
	/**
	 * 时间间隔
	 */
	private double dt;
	/**
	 * 速度
	 */
	private double velocity;
	/**
	 * 绘制点的最小数量，默认是100
	 */
	private double minPoints = 100;
	
	/**
	 * 保存点
	 */
	private ArrayList<Point> array = new ArrayList<Point>(); 
	
	private static final int SIZE = 240;
	
	private int start1;
	private int stop1;
	private int start2;
	private int stop2;
	
	private boolean done = false;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Example2 e2 = new Example2();
		e2.start();
	}
	
	public void start() {
		this.mp = new MyPanel();
		this.getContentPane().add(this.mp);
		this.setTitle("Expamle 4-3");
		
		this.dt 	  = 0.002;
		this.velocity = 2 * Math.PI;
		
		start1 = 50;
		stop1  = 200;
		start2 = 300;
		stop2  = 450;
		
		new Thread(new PaintThread()).start();
	}
	
	/**
	 * 验证第二定律
	 */
	private void calc2() {
		Sun sun = new Sun();
		sun.setPosX(0);
		sun.setPosY(0);
		
		Earth earth = new Earth();
		//假设初始时，地球在 x 轴正半轴上，距离以 AU 为单位
		earth.setPosX(1);
		earth.setPosY(0);
		//初始速度
		earth.setSpeedX(0);
		earth.setSpeedY(velocity);
		
		double r;
		double x;
		double y;
		double vx;
		double vy;
		
		int i = 0;
		Color defaultColor = new Color(170,170,170);
		
		// 至少循环一圈
		while(i < 1 / dt || !(earth.getPosY() > 0 && earth.getPosX() > 0) || i < minPoints) {
			// 计算距离
			r = Calc.distance(sun.getPosX(), sun.getPosY(), earth.getPosX(), earth.getPosY());
			// 用不同颜色绘图
			if(i >= start1 && i < stop1) {
				setPos(earth.getPosX() * SIZE, earth.getPosY() * SIZE, Color.red);
			}
			else if(i >= start2 && i < stop2) {
				setPos(earth.getPosX() * SIZE, earth.getPosY() * SIZE, Color.BLUE);
			}
			else {
				setPos(earth.getPosX() * SIZE, earth.getPosY() * SIZE, defaultColor);
			}
			
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mp.repaint();
			//对应 example 4.1 上的伪算法，4.7 的公式
			//速度变化
			vx = Calc.getNextVi(earth.getSpeedX(), earth.getPosX(), r, dt);
			earth.setSpeedX(vx);
			vy = Calc.getNextVi(earth.getSpeedY(), earth.getPosY(), r, dt);
			earth.setSpeedY(vy);
			//位置变化
			x = Calc.getNextPos(earth.getPosX(), earth.getSpeedX(), dt);
			earth.setPosX(x);
			y = Calc.getNextPos(earth.getPosY(), earth.getSpeedY(), dt);
			earth.setPosY(y);
			i++;
		}
		mp.repaint();
		done = true;
		System.out.println("done!");
	}
	
	private class PaintThread implements Runnable {
		@Override
		public void run() {
			calc2();	
		}
	}
	
	private class MyPanel extends JPanel{
		public void paint(Graphics g) {
			super.paint(g);
			paintExample(g);
			if(done) {
				paintArea(g);
			}
		}
	}
	
	/**
	 * 向 array 中添加 Point 元素
	 * @param x
	 * @param y
	 * @param color
	 */
	private void setPos(double x, double y, Color color) {
		Point p = new Point(x, y, color);
		this.array.add(p);
	}
	
	/**
	 * 绘制图形
	 * @param g
	 */
	private void paintExample(Graphics g) {
		g.translate(600, 340);
		// 画出轨迹
		for(int i = 0; i < array.size(); i++) {
			Point p = array.get(i);
			g.setColor(p.getColor());
			int x = (int) p.getX();
			int y = (int) p.getY();
			drawCenter(g, x, y, 3);
		}				

		
		g.setColor(Color.BLACK);
		// 画出中心
		g.fillOval(-7, -7, 14, 14);
		g.drawString("solar", 8, -8);
		// 画出坐标系
		g.drawLine(-SIZE, SIZE, SIZE + 50, SIZE); //x 轴
		g.drawLine(-SIZE, -SIZE -50, -SIZE, SIZE);  //y 轴
		g.drawString("x (AU)", 0, SIZE + 35);
		g.drawString("y (AU)", -SIZE -55, 0);
		// 画出坐标点
		g.drawString("0", 0, SIZE + 15);
		g.drawString("1", SIZE, SIZE + 15);
		g.drawString("-1", -SIZE, SIZE + 15);
		g.drawString("0", -SIZE -15, 0);
		g.drawString("-1", -SIZE -15, SIZE);
		g.drawString("1", -SIZE -15, -SIZE);
		
		// 显示信息
		g.translate(-600, -340);
		g.drawString("timestep = " + dt + "  yr", 100, 15);
		g.drawString("velocity    = " + velocity/ Math.PI + " π AU/yr", 100, 30);
		
	}
	
	/**
	 * 绘制扫过区域
	 * @param g
	 */
	public void paintArea(Graphics g) {
		// 将扫过的面积绘成相应颜色
		g.translate(600, 340);
		int area1 = 0;
		int area2 = 0;
		//计算红色面积并绘制
		if(stop1  < array.size()) {
			area1 = 0;
			
			//如果 stop 线条可以绘制出的话，开始计算围线所包围的面积
			ArrayList<Point> al = new ArrayList<Point>();
			for(int i = start1; i < stop1; i++) {
				al.add(array.get(i));
			}
			int[] res = calcMaxMin(al);
//			System.out.println("red yM: " + res[1] + "  ym: " + res[3]);
//			System.out.println("red xM: " + res[0] + "  xm: " + res[2]);
			
			g.setColor(array.get(start1 + 1).getColor());
			// 计算扫过的区域
			for(int i = -SIZE - 10; i <= SIZE + 10; i++) {
				for(int j = -SIZE - 10; j <= SIZE + 10; j++) {
					int podis = (int) Calc.distance(0, 0, i, j);
					int x =(int) (SIZE * i / (podis == 0 ? 1 : podis));
					//由于 Y 轴最大值 238， 最小值 -241 ，所以加上一个 -1.5 的偏移
					int y =(int) (SIZE * j / (podis == 0 ? 1 : podis) - 1.5) ;
					
					if((x >= res[2] && x <= res[0]) && (y >= res[3] && y <= res[1]) && podis <= SIZE) {
						g.fillRect(i, j, 1, 1);
						area1++;
					}
					
				}
			}
		}
		//计算蓝色面积并绘制
		if(stop2  < array.size()) {
			area2 = 0;
			g.setColor(array.get(start2 + 1).getColor());
			//如果stop可以绘制出的话，开始计算围线所包围的面积
			ArrayList<Point> al = new ArrayList<Point>();
			for(int i = start2; i < stop2; i++) {
				al.add(array.get(i));
			}
			int[] res = calcMaxMin(al);
			System.out.println("blue yM: " + res[1] + "  ym: " + res[3]);
			System.out.println("blue xM: " + res[0] + "  xm: " + res[2]);
			// 计算扫过的区域
			for(int i = -SIZE - 10; i <= SIZE + 10; i++) {
				for(int j = -SIZE - 10; j <= SIZE + 10; j++) {
					int podis = (int) Calc.distance(0, 0, i, j);
					int x =(int) (SIZE * i / (podis == 0 ? 1 : podis));
					int y =(int) (SIZE * j / (podis == 0 ? 1 : podis) - 1.5) ;
					if((x >= res[2] && x <= res[0]) && (y >= res[3] && y <= res[1]) && podis <= SIZE) {
						g.drawRect(i, j, 1, 1);
						area2++;
					}
				}
			}
		}
		
		// 由于使用线条计算面积会造成中间很多像素无法计算
//		for(int i = start1 + 1; i < stop1; i++) {
//			if(i < array.size()) {
//				Point startPoint1 = array.get(i);
//				g.drawLine(0, 0, (int) startPoint1.getX(), (int) startPoint1.getY());
//			}
//		}
//		
//		for(int i = start2 + 1; i < stop2; i++) {
//			if(i < array.size()) {
//				Point startPoint2 = array.get(i);
//				g.drawLine(0, 0, (int) startPoint2.getX(), (int) startPoint2.getY());
//			}
//		}
		// 写出不同区域的面积
		g.translate(-600, -340);
		g.setColor(Color.BLACK);
		g.drawString("red area square: "+ area1, 100, 45);
		g.drawString("blue area square: "+ area2, 100, 60);
	}

	/**
	 *  计算 array 中的最大值最小值
	 *  @return  
	 *  	res[0] = xMax
	 *  	res[1] = yMax
	 *  	res[2] = xMin
	 *  	res[3] = yMin
	 */
	private int[] calcMaxMin(ArrayList<Point> al) {
		int yM = -SIZE;
		int xM = -SIZE;
		int ym = SIZE;
		int xm = SIZE;
		for(Point p : al) {
			if(yM < p.getY()) {
				yM = (int)p.getY();
			}
			if(xM < p.getX()) {
				xM = (int)p.getX();
			}
			if(ym > p.getY()) {
				ym = (int)p.getY();
			}
			if(xm > p.getX()) {
				xm = (int)p.getX();
			}
		}
//		System.out.println("yM : " + yM + "  ym: " + ym);
//		System.out.println("xM : " + xM + "  xm: " + xm);
		int[] res = {xM, yM, xm, ym};
		return res;
	}
}
