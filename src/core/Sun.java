package core;

public class Sun extends VecPlanet{
	public Sun() {
		setName("Sun");
		//不能直接以 kg 为单位
//		setMass(Const.SUN_MASS);
		setMass(10);
	}
}
