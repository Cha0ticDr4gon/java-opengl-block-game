package Entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Models.WireframeModel;
import Utilities.MathUtils;

public class WireframeEntity {
	
	public WireframeModel model;
	
	public Vector3f position;
	public Vector3f rotation;
	public Vector3f scale;
	public Vector3f color;
	
	WireframeEntity(WireframeModel model, Vector3f position, Vector3f color) {
		this.model = model;
		this.position = position;
		this.color = color;
		rotation = new Vector3f(0.0f, 0.0f, 0.0f);
		scale = new Vector3f(1.0f, 1.0f, 1.0f);
	}
	
	WireframeEntity(WireframeModel model, Vector3f position, Vector3f scale, Vector3f color) {
		this.model = model;
		this.position = position;
		this.scale = scale;
		this.color = color;
		rotation = new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	public Matrix4f getTransform() {
		return MathUtils.createTransformationMatrix(position, rotation, scale);
	}

}
