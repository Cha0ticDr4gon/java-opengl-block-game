package Utilities;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import Models.Model;

public class ModelUtils {
	
	static HashMap <Integer, Integer[]> vaoMap = new HashMap<Integer, Integer[]>();
	
	public static Model createNewModel(float[] vertices, float[] normals) {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		int vertexID = loadDataToVBO(vertices, 3, 0);
		int normalID = loadDataToVBO(normals, 3, 0);
		GL30.glBindVertexArray(0);
		
		vaoMap.put(vaoID, new Integer[] {vertexID, normalID});
		
		return new Model(vaoID, vertices.length / 3);
	}
	
	private static int loadDataToVBO(float[] data, int dimension, int location) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(vboID, GL15.GL_ARRAY_BUFFER);
		FloatBuffer buffer = FloatBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(location, dimension, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	public void destroyModel(Model model) {
		for(int vboID : vaoMap.get(model.vaoID)) {
			GL15.glDeleteBuffers(vboID);
		}
		GL30.glDeleteVertexArrays(model.vaoID);
	}
	
	public void destroyAll() {
		for(int vaoID : vaoMap.keySet()) {
			for(int vboID : vaoMap.get(vaoID)) {
				GL15.glDeleteBuffers(vboID);
			}
			GL30.glDeleteVertexArrays(vaoID);
		}
	}

}
