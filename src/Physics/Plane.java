package Physics;

public class Plane {
	
	public Vector3 p0;
	public Vector3 p1;
	public Vector3 p2;
	public Vector3 p3;
	
	public Plane(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public Vector3 getNormal() {
		return Vector3.crossProduct(Vector3.subtract(p1, p0), Vector3.subtract(p3, p0)).getNormalized();
	}

}
