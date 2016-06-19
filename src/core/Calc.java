package core;

/**
 * 计算常用的公式的结果
 * @author future
 *
 */
public class Calc {
	
	/**
	 * 计算两点之间的距离
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return 两点之间的距离
	 */
	public static double distance(double x1, double y1, double x2, double y2) {
		double x = Math.abs(x1 - x2);
		double y = Math.abs(y1 - y2);
		double dis = Math.sqrt(x * x + y * y);
		return dis;
	}
	
	/**
	 * 计算万有引力(矢量), 从 2 指向 1,即 行星 2 所受的万有引力
	 * @param M1 行星 1 的质量
	 * @param M2 行星 2 的质量
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return 
	 */
	public static MyVector getGravity(double M1, double M2, double x1, double y1, double x2, double y2) {
		double r = distance(x1, y1, x2, y2);
		double FG = Const.G * M1 * M2 / (r * r);
//		System.out.println("质量M1=" + M1);
//		System.out.println("质量M2=" + M2);
//		System.out.println("FG=" + FG);
		
		MyVector mv = new MyVector();
		mv.setLength(FG);
		Point direct = new Point(x1 - x2, y1 - y2);
		mv.setDirect(direct);
//		return FG;
		return mv;
	}

	/**
	 * 计算万有引力的大小
	 * @param M1
	 * @param M2
	 * @param r
	 * @return
	 */
	public static double calcGravity(double M1, double M2, double r) {
		double FG = Const.G * M1 * M2 / (r * r);
		return FG;
	}

	public static MyVector getAcce(MyVector F, double M) {
		MyVector mv = new MyVector();
		mv.setLength(F.getLength() / M);
		mv.setDirect(F.getDirect());
		return mv;
	}
	
	
	
	/**
	 * 修正万有引力中距离的幂
	 * @param Ms
	 * @param Me
	 * @param r
	 * @param beta
	 * @return
	 */
	public static double modGravity(double Ms, double Me, double r, double beta) {
		double FG = Const.G * Ms * Me / Math.pow(r, beta);
		return FG;
	}
	
	/**
	 * 计算矢量大小
	 * @param vx  x 分量
	 * @param vy  y 分量
	 * @return    合成的矢量大小
	 */
	public static double getVec(double vx, double vy) {
		double v = Math.sqrt(vx * vx + vy * vy);
		return v;
	}
	
	/**
	 * 计算开普勒第三定律， T<sup>2</sup>/a <sup>3</sup>
	 * @param  T  周期
	 * @param  a  半长轴
	 * @return
	 */
	public static double calcKepler3(double T, double a) {
		return T * T / Math.pow(a, 3);
	}
	
	/**
	 * 计算约化质量
	 * @param m1
	 * @param m2
	 * @return
	 */
	public static double calcReducedMass(double m1, double m2) {
		return m1 * m2 / ( m1 + m2 );
	}
	
	/**
	 * 使用 Euler-Cromer 法则计算下一次的速度分量
	 * @param vi 第 i 次的速度分量
	 * @param pi 第 i 次坐标分量
	 * @param ri 第 i 次距离
	 * @param dt 时间间隔
	 * @return   第 i+1 次的速度分量
	 */
	public static double getNextVi(double vi, double pi, double ri, double dt) {
		double vin = vi - 4 * Math.PI * Math.PI* pi * dt / Math.pow(ri, 3);
		return vin;
	}
	
	/**
	 * 使用 Euler-Cromer 法则计算下一次的位置
	 * @param pi  第 i 次坐标分量 
	 * @param vin 第 i+1 次的速度分量
	 * @param dt  时间间隔
	 * @return    第 i+1 次的坐标分量
	 */
	public static double getNextPos(double pi, double vin, double dt) {
		double pni = pi + vin * dt;
		return pni;
	}
	
	/**
	 * 计算半径，假定 theta0 = 0
	 * @param Ms
	 * @param Mp
	 * @param theta
	 * @param L
	 * @param e
	 * @return
	 */
	public static double getRadius(double Ms, double Mp, double theta, double L, double e) {
		double u = Calc.calcReducedMass(Ms, Mp);
//		double L = u * r * r * theta;
		// 对应 4.10 公式
		double r = (L * L / (u * Const.G * Ms * Mp)) / (1 - e * Math.cos(theta));
		
		return r;
	}
	
	/**
	 * 得到行星在椭圆轨道上的最大速度
	 * @param Ms
	 * @param Mp
	 * @param a
	 * @param e
	 * @return
	 */
	public static double getVmax(double Ms, double Mp, double a, double e) {
		//对应 4.11  公式
		double Vmax = Math.sqrt(Const.G * Ms * (1 + e) * (1 + Mp / Ms) / a / (1 - e));
		return Vmax;
	}
	
	/**
	 * 得到行星在椭圆轨道上的最小速度
	 * @param Ms
	 * @param Mp
	 * @param a
	 * @param e
	 * @return
	 */
	public static double getVmin(double Ms, double Mp, double a, double e) {
		//对应 4.11  公式
		double Vmin = Math.sqrt(Const.G * Ms * (1 - e) * (1 + Mp / Ms) / a / (1 + e));
		return Vmin;
	}
}
