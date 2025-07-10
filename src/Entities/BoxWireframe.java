package Entities;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import Models.WireframeModel;

public class BoxWireframe extends WireframeEntity{
	
	private static Vector3f[] vertices = {new Vector3f(1, 1, 1), 
										  new Vector3f(1, 1, -1), 
										  new Vector3f(-1, 1, -1), 
										  new Vector3f(-1, 1, 1), 
										  
										  new Vector3f(1, -1, 1), 
										  new Vector3f(1, -1, -1), 
										  new Vector3f(-1, -1, -1), 
										  new Vector3f(-1, -1, 1)};
	
	public BoxWireframe(Vector3f position, Vector3f scale, Vector3f color) {
		super(new WireframeModel(generateModelVAO(), 24), position, scale, color);
	}
	
	private static int generateModelVAO() {
		float[] positions = new float[12 * 2 * 3];
		int index = 0;
		
		index = writeLine(vertices[0], vertices[1], positions, index);
		index = writeLine(vertices[1], vertices[2], positions, index);
		index = writeLine(vertices[2], vertices[3], positions, index);
		index = writeLine(vertices[3], vertices[0], positions, index);
		
		index = writeLine(vertices[4], vertices[5], positions, index);
		index = writeLine(vertices[5], vertices[6], positions, index);
		index = writeLine(vertices[6], vertices[7], positions, index);
		index = writeLine(vertices[7], vertices[4], positions, index);
		
		index = writeLine(vertices[0], vertices[4], positions, index);
		index = writeLine(vertices[1], vertices[5], positions, index);
		index = writeLine(vertices[2], vertices[6], positions, index);
		index = writeLine(vertices[3], vertices[7], positions, index);
		
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(positions.length);
		buffer.put(positions);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL30.glBindVertexArray(0);
		return vaoID;
	}
	
	private static int writeLine(Vector3f start, Vector3f end, float[] positions, int index) {
		positions[index] = start.x;
		index++;
		positions[index] = start.y;
		index++;
		positions[index] = start.z;
		index++;
		positions[index] = end.x;
		index++;
		positions[index] = end.y;
		index++;
		positions[index] = end.z;
		index++;
		return index;
	}

}
