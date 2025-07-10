package Physics;

public class BoxCollider {
	
	public Vector3 position;
	public Vector3 rotation;
	public Vector3 scale;
	
	private Vector3[] positions = {new Vector3(1, 1, 1), 
								   new Vector3(1, 1, -1), 
								   new Vector3(-1, 1, -1), 
								   new Vector3(-1, 1, 1), 
								   new Vector3(1, -1, 1), 
								   new Vector3(1, -1, -1), 
								   new Vector3(-1, -1, -1), 
								   new Vector3(-1, -1, 1)};
	
	public Vector3[] prev = new Vector3[8];
	public Vector3[] next = new Vector3[8];
	
	Plane prevPlaneTop;
	Plane prevPlaneBottom;
	Plane prevPlaneLeft;
	Plane prevPlaneRight;
	Plane prevPlaneFront;
	Plane prevPlaneBack;
	
	Plane nextPlaneTop;
	Plane nextPlaneBottom;
	Plane nextPlaneLeft;
	Plane nextPlaneRight;
	Plane nextPlaneFront;
	Plane nextPlaneBack;
	
	public BoxCollider(Vector3 position, Vector3 rotation, Vector3 scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		
		for(int i = 0; i < 8; i++) {
			prev[i] = new Vector3(0, 0, 0);
			next[i] = new Vector3(0, 0, 0);
		}
		
		prevPlaneTop = new Plane(prev[0], prev[1], prev[2], prev[3]);
		prevPlaneBottom = new Plane(prev[4], prev[7], prev[6], prev[5]);
		prevPlaneLeft = new Plane(prev[3], prev[2], prev[6], prev[7]);
		prevPlaneRight = new Plane(prev[0], prev[4], prev[5], prev[1]);
		prevPlaneFront = new Plane(prev[0], prev[3], prev[7], prev[4]);
		prevPlaneBack = new Plane(prev[2], prev[1], prev[5], prev[6]);
		
		nextPlaneTop = new Plane(next[0], next[1], next[2], next[3]);
		nextPlaneBottom = new Plane(next[4], next[7], next[6], next[5]);
		nextPlaneLeft = new Plane(next[3], next[2], next[6], next[7]);
		nextPlaneRight = new Plane(next[0], next[4], next[5], next[1]);
		nextPlaneFront = new Plane(next[0], next[3], next[7], next[4]);
		nextPlaneBack = new Plane(next[2], next[1], next[5], next[6]);
		
		transform();
		printTransformedPositions();
		applyTransform();
	}
	
	public void transform() {
		double xRadians = Math.toRadians(rotation.x);
		double yRadians = Math.toRadians(rotation.y);
		double zRadians = Math.toRadians(rotation.z);
		Matrix3 xRot = new Matrix3(new double[] {1, 0, 0, 0, Math.cos(xRadians), -Math.sin(xRadians), 0, Math.sin(xRadians), Math.cos(xRadians)});
		Matrix3 yRot = new Matrix3(new double[] {Math.cos(yRadians), 0, Math.sin(yRadians), 0, 1, 0, -Math.sin(yRadians), 0, Math.cos(yRadians)});
		Matrix3 zRot = new Matrix3(new double[] {Math.cos(zRadians), -Math.sin(zRadians), 0, Math.sin(zRadians), Math.cos(zRadians), 0, 0, 0, 1});
		Matrix3 transformation = Matrix3.multiply(zRot, Matrix3.multiply(yRot, xRot));
		for(int i = 0; i < 8; i++) {
			transformation.transform(positions[i], position, scale, next[i]);
		}
	}
	
	public void applyTransform() {
		for(int i = 0; i < 8; i++) {
			prev[i].x = next[i].x;
			prev[i].y = next[i].y;
			prev[i].z = next[i].z;
		}
	}
	
	public Vector3[] getTransformedVertices() {
		Vector3[] vertices = new Vector3[8];
		double xRadians = Math.toRadians(rotation.x);
		double yRadians = Math.toRadians(rotation.y);
		double zRadians = Math.toRadians(rotation.z);
		Matrix3 xRot = new Matrix3(new double[] {1, 0, 0, 0, Math.cos(xRadians), -Math.sin(xRadians), 0, Math.sin(xRadians), Math.cos(xRadians)});
		Matrix3 yRot = new Matrix3(new double[] {Math.cos(yRadians), 0, Math.sin(yRadians), 0, 1, 0, -Math.sin(yRadians), 0, Math.cos(yRadians)});
		Matrix3 zRot = new Matrix3(new double[] {Math.cos(zRadians), -Math.sin(zRadians), 0, Math.sin(zRadians), Math.cos(zRadians), 0, 0, 0, 1});
		Matrix3 transformation = Matrix3.multiply(zRot, Matrix3.multiply(yRot, xRot));
		for(int i = 0; i < 8; i++) {
			vertices[i] = new Vector3(0, 0, 0);
			transformation.transform(positions[i], new Vector3(0, 0, 0), scale, vertices[i]);
		}
		return vertices;
	}
	
	private void printTransformedPositions() {
		System.out.print("Box vertices: ");
		for(int i = 0; i < 8; i++) {
			printVector(next[i]);
			if(i < 7) System.out.print(", ");
		}
		System.out.println();
	}
	
	private void printVector(Vector3 vector) {
		System.out.print("[" + vector.x + ", " + vector.y + ", " + vector.z + "]");
	}

}
