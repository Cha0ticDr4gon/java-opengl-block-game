package Physics;

public class PlaneCollider {
	
	Vector3 position;
	Vector3 rotation;
	Vector3 scale = new Vector3(1.0, 1.0, 1.0);
	
	Vector3 normal;
	
	PlaneCollider(Vector3 position, Vector3 rotation) {
		this.position = position;
		this.rotation = rotation;
	}
}
