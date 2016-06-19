package main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import core.Calc;
import core.Const;
import core.Earth;
import core.MyVector;
import core.Point;
import core.Sun;

public class Example3 extends Example{

	private double dt = 0;
	private static final int SIZE = 240;
	private static final int UNIT = 50;
	private Sun sun;
	private Earth earth;
	private double r = 0;
	/**
	 * Graphics of panel
	 */
	private MyPanel mp;

	
	public void start() {
		dt  = 0.000001;
		
		sun = new Sun();
		//设置太阳的速度
		MyVector mvs = new MyVector();
		mvs.setDirect(new Point(0, 0));
		mvs.setLength(0);
		sun.setV(mvs);
		//设置位置
		MyVector mvps = new MyVector();
		mvps.setDirect(new Point(0, 0));
		mvps.setLength(0);
		sun.setPos(mvps);
		//设置质量
		sun.setMass(4 * Math.PI * Math.PI / Const.G);
//		sun.setMass(Const.SUN_MASS / Const.AU);
		
		earth = new Earth();
		//设置地球的速度
		MyVector mve = new MyVector();
		mve.setDirect(new Point(0,1));
		mve.setLength(2 * Math.PI);
		earth.setV(mve);
		//假设初始时，地球在 x 轴正半轴上，距离以 AU 为单位
		MyVector mvpe = new MyVector();
		mvpe.setDirect(new Point(1, 0));
		mvpe.setLength(1);
		earth.setPos(mvpe);
//		earth.setMass(sun.getMass() * Math.pow(10, -6));
//		earth.setMass(Const.EARTH_MASS / Const.AU);

		mp = new MyPanel();
		this.getContentPane().add(mp);
		new Thread(new PaintThread()).start();
	}
	
	private class PaintThread implements Runnable {

		@Override
		public void run() {
			int i = 0;
			try {
				Thread.sleep(12000);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			while( i < 200) {
//				System.out.println("earth  ==== x: " + earth.getVecPosX() + "   y: " + earth.getVecPosY());
//				System.out.println("earth VV  ==== x: " + earth.getVX() + "   y: " + earth.getVY());
				System.out.println();
				i++;
				r = Calc.distance(sun.getVecPosX(), sun.getVecPosY(), earth.getVecPosX(), earth.getVecPosY());
				// 地球的受力
				MyVector Fe = Calc.getGravity(sun.getMass(), earth.getMass(), sun.getVecPosX(), sun.getVecPosY(), earth.getVecPosX(), earth.getVecPosY());
				// 太阳的受力
				MyVector Fs = Calc.getGravity(earth.getMass(), sun.getMass(), earth.getVecPosX(), earth.getVecPosY(), sun.getVecPosX(), sun.getVecPosY());
				
				MyVector ae = Calc.getAcce(Fe, earth.getMass());
//				System.out.println("ae: " + ae.getLength());
				
				MyVector as = Calc.getAcce(Fs, sun.getMass());
				//改变速度和位移
				earth.nextPos(dt);
				earth.nextV(ae, dt);
				
				sun.nextPos(dt);
				sun.nextV(as, dt);
				
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				mp.repaint();
			}
		}
	}
	
	private class MyPanel extends JPanel{
		public void paint(Graphics g) {
			super.paint(g);
			paintBase(g);
			paintPlanet(g);
		}
	}
	
	private void paintPlanet(Graphics g) {
		g.translate(600, 340);
		g.setColor(Color.RED);
		int size = 12;
		g.fillOval((int) sun.getVecPosX() * UNIT, (int) sun.getVecPosY() * UNIT, size, size);
		g.drawString(sun.getName(), (int) sun.getVecPosX() * UNIT - 10, (int) sun.getVecPosY() * UNIT - 10);
		
		g.setColor(Color.BLACK);
		g.fillOval((int) earth.getVecPosX() * UNIT, (int) earth.getVecPosY() * UNIT, size, size);
		g.drawString(earth.getName(), (int) earth.getVecPosX() * UNIT + 15, (int) earth.getVecPosY() * UNIT + 15);
	}
	/**
	 * 绘制图形
	 * @param g
	 */
	private void paintBase(Graphics g) {
		g.translate(600, 340);

		
		g.setColor(Color.BLACK);
//		// 画出中心
//		g.fillOval(-7, -7, 4, 4);
//		g.drawString("solar", 8, -8);
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
		g.drawString("timestep = " + dt + "  yr", 30, 15);
		g.drawString("velocity of earth   = " + String.format("%.8f", earth.getV().getLength() / Math.PI) + " π AU/yr", 30, 30);
		g.drawString("velocity of sun     = " + String.format("%.16f",sun.getV().getLength() / Math.PI) + " π AU/yr", 30, 45);
		g.drawString("地球太阳的距离为：  " + r + " π AU/yr", 30, 60);
		g.drawString("太阳的位置为 ", 30, 90);
		g.drawString("	X：  " + String.format("%.8f",sun.getVecPosX()) + " AU  Y： " + String.format("%.8f",sun.getVecPosY()) + " AU", 30, 105);
		g.drawString("地球的位置为", 30, 120);
		g.drawString("	X：  " + String.format("%.8f",earth.getVecPosX()) + " AU  Y： " + String.format("%.8f",earth.getVecPosY()) + " AU", 30, 135);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Example3 e3 = new Example3();
		e3.start();
	}
}
