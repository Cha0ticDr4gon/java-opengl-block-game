package Physics;

public class Vector3 {
	
	public double x;
	public double y;
	public double z;
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getMagnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public void setNormalized() {
		double length = Math.sqrt(x * x + y * y + z * z);
		if(length == 0) return;
		x /= length;
		y /= length;
		z /= length;
	}
	
	public Vector3 getNormalized() {
		Vector3 vector = new Vector3(0, 0, 0);
		double length = Math.sqrt(x * x + y * y + z * z);
		if(length == 0) return vector;
		vector.x = x / length;
		vector.y = y / length;
		vector.z = z / length;
		return vector;
	}
	
	public static double dotProduct(Vector3 a, Vector3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	public static Vector3 crossProduct(Vector3 a, Vector3 b) {
		return new Vector3(a.y * b.z - a.z * b.y, 
						  a.z * b.x - a.x * b.z, 
						  a.x * b.y - a.y * b.x);
	}
	
	public static void normalize(Vector3 vector) {
		double length = Math.sqrt(vector.x * vector.x + vector.y * vector.y + vector.z * vector.z);
		if(length == 0) return;
		vector.x /= length;
		vector.y /= length;
		vector.z /= length;
	}
	
	public static void scale(Vector3 vector, double scalar) {
		vector.x *= scalar;
		vector.y *= scalar;
		vector.z *= scalar;
	}
	
	public static Vector3 subtract(Vector3 a, Vector3 b) {
		return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	
	public static Vector3 add(Vector3 a, Vector3 b) {
		return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
	}

}
