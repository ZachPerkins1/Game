
public class Util {
	public static double EPSILON = 0.05;
	
	public static boolean equals(double a, double b) {
		return a > b - EPSILON && a < b + EPSILON;
	}
}
