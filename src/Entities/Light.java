package Entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	public Vector3f position;
	public Vector3f color;
	public float intensity = 1.0f;
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}

}
