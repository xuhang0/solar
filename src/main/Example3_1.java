package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.Calc;
import core.Const;
import core.Earth;
import core.MyVector;
import core.Point;
import core.Sun;

/**
 * 改进 example 3
 * @author future
 *
 */
public class Example3_1 extends Example{

	private double dt = 0;
	private static final int SIZE = 240;
	private static final int UNIT = 240;
	private Sun sun;
	private Earth earth;
	private double r = 0;
	private ArrayList<Point> trace = new ArrayList<Point>();
	
	private JButton control = new JButton("开始");
	private JTextField vtext = new JTextField(10);
	private boolean signal = false;
	
	/**
	 * Graphics of panel
	 */
	private MyPanel mp;

	
	public void start() {
		initUI();
		dt  = 0.00005;
		sun = new Sun();
		//设置太阳的速度
		MyVector mvs = new MyVector();
		mvs.setDirect(new Point(0, -1));
		mvs.setLength(Math.sqrt(Const.G));
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

		
	}
	
	private class PaintThread implements Runnable {

		@Override
		public void run() {
			System.out.println("earth pos : " + earth.getVecPosX() + " === " + earth.getVecPosY());
			System.out.println("earth v : " + earth.getVX() + " === " + earth.getVY());
			System.out.println();

			while(signal) {
//				System.out.println("earth  ==== x: " + earth.getVecPosX() + "   y: " + earth.getVecPosY());
//				System.out.println("earth VV  ==== x: " + earth.getVX() + "   y: " + earth.getVY());
//				System.out.println();

				r = Calc.distance(sun.getVecPosX(), sun.getVecPosY(), earth.getVecPosX(), earth.getVecPosY());
//				System.out.println("距离 R = " + r);
				// 地球的受力
				MyVector Fe = Calc.getGravity(sun.getMass(), earth.getMass(), sun.getVecPosX(), sun.getVecPosY(), earth.getVecPosX(), earth.getVecPosY());
				// 太阳的受力
				MyVector Fs = Calc.getGravity(earth.getMass(), sun.getMass(), earth.getVecPosX(), earth.getVecPosY(), sun.getVecPosX(), sun.getVecPosY());
				
				MyVector ae = Calc.getAcce(Fe, earth.getMass());
//				System.out.println("ae: " + ae.getLength() +" ==== " + ae.getDirect().getX() + " === " + ae.getDirect().getY());
				
				MyVector as = Calc.getAcce(Fs, sun.getMass());
				//改变速度和位移
				earth.nextPos(dt);
				earth.nextV(ae, dt);
				//添加轨迹
				trace.add(new Point(earth.getVecPosX(), earth.getVecPosY()));
//				System.out.println("earth pos next: " + earth.getPos().getLength() + " === " + earth.getVecPosX() + " === " + earth.getVecPosY());
//				System.out.println("earth v next: " + earth.getV().getLength() + " === " + earth.getVX() + " === " + earth.getVY());
//				System.out.println();
				
				sun.nextPos(dt);
				sun.nextV(as, dt);
//				System.out.println("sun pos next: " + sun.getPos().getLength() + " === " + sun.getVecPosX() + " === " + sun.getVecPosY());
//				System.out.println("sun v next: " + sun.getV().getLength() + " === " + sun.getVX() + " === " + sun.getVY());
//				System.out.println();
				
				try {
					Thread.sleep(3);
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
	
	public void initUI() {
//		this.add(control, BorderLayout.EAST);
		mp = new MyPanel();
		mp.setLayout(null);
		mp.add(control);
		mp.add(vtext);
		vtext.setText("2");
		vtext.setBounds(50, 560, 30, 30);;
		control.setBounds(50, 600, 80, 50);
		this.getContentPane().add(mp);
		mp.repaint();
		this.repaint();
		this.control.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MyVector mve = new MyVector();
				mve.setDirect(new Point(0,1));
				mve.setLength(Double.valueOf(vtext.getText()) * Math.PI);
				earth.setV(mve);
				vtext.setEnabled(false);
				signal = !signal;
				if(!signal) {
					control.setText("start");
				} else {
					control.setText("stop");
				}
				new Thread(new PaintThread()).start();
			}
		});
	}
	
	private void paintPlanet(Graphics g) {
		int size = 12;
		g.translate(600 , 340 );
		//画地球的轨迹
		g.setColor(Color.GREEN);
		for(int i = 0; i < trace.size(); i++) {
			Point p = trace.get(i);
//			System.out.println("px: "+ p.getX() + "  py: " + p.getY());
			g.fillOval((int) (p.getX() * UNIT), (int) (p.getY() * UNIT), 2, 2);
		}
		g.translate(- size / 2, - size / 2);
		//画出太阳
		g.setColor(Color.RED);
		g.fillOval((int) (sun.getVecPosX() * UNIT), (int) (sun.getVecPosY() * UNIT), size, size);
		g.drawString(sun.getName(), (int) (sun.getVecPosX() * UNIT) - 10, (int) (sun.getVecPosY() * UNIT) - 10);
		//画出地球
		g.setColor(Color.BLACK);
		g.fillOval((int) (earth.getVecPosX() * UNIT), (int) (earth.getVecPosY() * UNIT), size, size);
		g.drawString(earth.getName(), (int) (earth.getVecPosX() * UNIT) + 15, (int) (earth.getVecPosY() * UNIT) + 15);
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
		g.drawString("	X：  " + String.format("%.10f",sun.getVecPosX()) + " AU  Y： " + String.format("%.10f",sun.getVecPosY()) + " AU", 30, 105);
		g.drawString("地球的位置为", 30, 120);
		g.drawString("	X：  " + String.format("%.8f",earth.getVecPosX()) + " AU  Y： " + String.format("%.8f",earth.getVecPosY()) + " AU", 30, 135);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Example3_1 e3 = new Example3_1();
		e3.start();
	}
}
