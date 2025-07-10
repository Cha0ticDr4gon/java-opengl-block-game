package Entities;

import org.lwjgl.util.vector.Vector3f;

import Utilities.MathUtils;

public class Ray {
	
	public Vector3f position;
	public Vector3f rotation;
	public Vector3f direction;
	
	public Ray(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
		direction = new Vector3f(0, 0, -1);
		updateDirection();
	}
	
	public void updateDirection() {
		MathUtils.rotateVector(direction, rotation.x, rotation.y, rotation.z);
	}

}
