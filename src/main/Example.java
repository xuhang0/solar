package main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Example extends JFrame{
	public static final int QUADRANT_1 = 1;
	public static final int QUADRANT_2 = 2;
	public static final int QUADRANT_3 = 3;
	public static final int QUADRANT_4 = 4;
	public Example() {
		this.setSize(800, 600);// 设置窗体的大小
		this.setLocation(300, 50);// 设置窗体的位置
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * 计算坐标所在点的象限，如果是在坐标轴上的话，返回0
	 * @param x
	 * @param y
	 * @return
	 */
	public int getQuadrant(int x, int y) {
		if(x > 0 && y > 0) {
			return QUADRANT_1;
		}
		if(x > 0 && y < 0) {
			return QUADRANT_4;
		}
		if(x < 0 && y > 0) {
			return QUADRANT_2;
		}
		if(x < 0 && y < 0) {
			return QUADRANT_3;
		}
		return 0;
	}
	
	/**
	 * 将像素描在中心处
	 * @param g 画笔
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @param size 大小
	 */
	public void drawCenter(Graphics g, int x, int y, int size) {
		if(x > 0) {
			if(y > 0) {
				g.fillRect(x - size/2, y - size/2, size, size);
			} else {
				g.fillRect(x - size/2, y + size/2, size, size);
			}
		} else {
			if(y > 0) {
				g.fillRect(x + size/2, y - size/2, size, size);
			} else {
				g.fillRect(x + size/2, y + size/2, size, size);
			}
		}
	}
}
