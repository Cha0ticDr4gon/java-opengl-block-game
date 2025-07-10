package GUI;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import Models.Model;
import Utilities.MathUtils;

public class GUIEntity {
	
	Model model;
	
	public Vector2f position;
	float rotation;
	public Vector2f dimension;
	
	public GUIEntity(Model model, Vector2f position, Vector2f dimension, float rotation) {
		this.model = model;
		this.position = position;
		this.dimension = dimension;
		this.rotation = rotation;
	}
	
	public Matrix4f getTransform() {
		return MathUtils.createGuiTransformationMatrix(position, rotation, dimension.x, dimension.y);
	}

}
