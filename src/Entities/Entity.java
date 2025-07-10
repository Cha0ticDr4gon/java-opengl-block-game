package Entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Models.Model;
import Utilities.MathUtils;

public class Entity {
	public Model model;
	public Vector3f position;
	public Vector3f rotation;
	public Vector3f scale;
	
	public Vector3f color = new Vector3f(1.0f, 1.0f, 1.0f);
	
	public Entity(Model model, Vector3f position) {
		this.model = model;
		this.position = position;
		rotation = new Vector3f(0.0f, 0.0f, 0.0f);
		scale = new Vector3f(1.0f, 1.0f, 1.0f);
	}
	
	public Entity(Model model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public void setColor(float r, float g, float b) {
		color.x = r;
		color.y = g;
		color.z = b;
	}
	
	public Matrix4f getTransform() {
		return MathUtils.createTransformationMatrix(position, rotation, scale);
	}
}
