package Entities;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Models.WireframeModel;
import Physics.BoxCollider;
import Physics.Vector3;
import Utilities.MathUtils;

public class ColliderMesh extends WireframeEntity{
	
	BoxCollider collider;
	
	public ColliderMesh(BoxCollider collider) {
		super(null, new Vector3f(0, 0, 0), new Vector3f(0.0f, 1.0f, 0.0f));
		this.collider = collider;
		model = new WireframeModel(generateMeshVAO(), 24);
		updatePosition();
		scale.x = 0.0001f;
		scale.y = 0.0001f;
		scale.z = 0.0001f;
	}
	
	public void updatePosition() {
		position.x = (float) collider.position.x;
		position.y = (float) collider.position.y;
		position.z = (float) collider.position.z;
	}
	
	public Matrix4f getTransform() {
		return MathUtils.createTransformationMatrix(position, 0, 0, 0, 1.0001f, 1.0001f, 1.0001f);
	}
	
	private int generateMeshVAO() {
		Vector3[] positions = collider.getTransformedVertices();
		float[] vertices = new float[12 * 2 * 3];
		int index = 0; 
		index = writeLine(positions[0], positions[1], vertices, index);
		index = writeLine(positions[1], positions[2], vertices, index);
		index = writeLine(positions[2], positions[3], vertices, index);
		index = writeLine(positions[3], positions[0], vertices, index);
		
		index = writeLine(positions[4], positions[5], vertices, index);
		index = writeLine(positions[5], positions[6], vertices, index);
		index = writeLine(positions[6], positions[7], vertices, index);
		index = writeLine(positions[7], positions[4], vertices, index);
		
		index = writeLine(positions[0], positions[4], vertices, index);
		index = writeLine(positions[1], positions[5], vertices, index);
		index = writeLine(positions[2], positions[6], vertices, index);
		index = writeLine(positions[3], positions[7], vertices, index);
		
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
		return vaoID;
	}
	
	private int writeLine(Vector3 start, Vector3 end, float[] vertices, int index) {
		vertices[index] = (float) start.x;
		index++;
		vertices[index] = (float) start.y;
		index++;
		vertices[index] = (float) start.z;
		index++;
		vertices[index] = (float) end.x;
		index++;
		vertices[index] = (float) end.y;
		index++;
		vertices[index] = (float) end.z;
		index++;
		return index;
	}

}
