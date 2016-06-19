package core;

public class MyVector {
	private double length;
	private Point direct = new Point(0, 0);
	
	/**
	 * 向量相加
	 * @param mv
	 */
	public MyVector add(MyVector mv) {
		double l1 = Calc.getVec(direct.getX(), direct.getY());
		double xl;
		double yl;
		
		if(l1 != 0) {
			xl = length * direct.getX() / l1;
			yl = length * direct.getY() / l1;
		} else {
			xl = 0;
			yl = 0;
		}
//		System.out.println("l1: " + l1 + " === xl: " + xl + " ===yl: " + yl);
//		System.out.println();
		double l2 = Calc.getVec(mv.direct.getX(), mv.direct.getY());
		double xl2;
		double yl2;
		if(l2 != 0) {
			xl2 = mv.length * mv.direct.getX() / l2;
			yl2 = mv.length * mv.direct.getY() / l2;
		} else {
			xl2 = 0;
			yl2 = 0;
		}
//		System.out.println("l2: " + l2 + " === xl2: " + xl2 + " ===yl2: " + yl2);
//		System.out.println();
		xl += xl2;
		yl += yl2;
		double l3 = Calc.getVec(xl, yl);
		
		MyVector v1 = new MyVector();
		if(l3 != 0) {
			v1.direct.setX(xl / l3);
			v1.direct.setY(yl / l3);
		} else {
			v1.direct.setX(0);
			v1.direct.setY(0);
		}
//		System.out.println("=====l3: " + l3 + " === dx: " + v1.getDirect().getX() + " ===dy: " + v1.getDirect().getY());
//		System.out.println();
		v1.length = Calc.getVec(xl, yl);
		return v1;
	}
	
	/**
	 * 与数字相乘
	 * @param mul
	 */
	public MyVector multi(double mul) {
		double l = length * mul;
		MyVector mv = new MyVector();
		mv.setDirect(direct);
		mv.setLength(l);
		return mv;
	}
	
	/**
	 * @return the length
	 */
	public double getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}
	/**
	 * @return the direct
	 */
	public Point getDirect() {
		return direct;
	}
	/**
	 * @param direct the direct to set
	 */
	public void setDirect(Point direct) {
		//归一化
		double l = Calc.getVec(direct.getX(), direct.getY());
		if(l != 0) {
			Point d = new Point(direct.getX() / l, direct.getY() / l);
			this.direct = d;
		} else {
			this.direct = direct;
		}
	}
	
}
