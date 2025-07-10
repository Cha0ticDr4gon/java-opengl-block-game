package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Models.Model;

public class ObjFileLoader {
	
	ArrayList <Vector3f> vertexArray = new ArrayList<Vector3f>();
	ArrayList <Vector3f> normalArray = new ArrayList<Vector3f>();
	ArrayList <Vector2f> textureArray = new ArrayList<Vector2f>();
	ArrayList <String> faceList = new ArrayList<String>();
	
	float[] vertices;
	float[] normals;
	float[] textureCoords;
	
	public Model loadTexturedModel(String fileName) {
		readFileData(fileName);
		vertices = new float[faceList.size() * 3 * 3];
		normals = new float[faceList.size() * 3 * 3];
		textureCoords = new float[faceList.size() * 2 * 2];
		for(int i = 0; i < faceList.size(); i++) {
			String[] face = faceList.get(i).split(" ");
			
			writeTexturedVertexData(face[0].split("/"), i * 3);
			writeTexturedVertexData(face[1].split("/"), i * 3 + 1);
			writeTexturedVertexData(face[2].split("/"), i * 3 + 2);
		}
		
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		loadDataToVBO(vertices, 0, 3);
		loadDataToVBO(normals, 1, 3);
		loadDataToVBO(textureCoords, 2, 2);
		GL30.glBindVertexArray(0);
		Model model = new Model(vaoID, faceList.size() * 3);
		
		return model;
	}
	
	public void writeTexturedVertexData(String[] values, int i) {
		int vertexIndex = Integer.parseInt(values[0]) - 1;
		int normalIndex = Integer.parseInt(values[2]) - 1;
		int textureIndex = Integer.parseInt(values[1]) - 1;
		
		Vector3f vertex = vertexArray.get(vertexIndex);
		vertices[i * 3] = vertex.x;
		vertices[i * 3 + 1] = vertex.y;
		vertices[i * 3 + 2] = vertex.z;
		
		Vector3f normal = normalArray.get(normalIndex);
		normals[i * 3] = normal.x;
		normals[i * 3 + 1] = normal.y;
		normals[i * 3 + 2] = normal.z;
		
		Vector2f textureCoord = textureArray.get(textureIndex);
		textureCoords[i * 2] = textureCoord.x;
		textureCoords[i * 2 + 1] = textureCoord.y;
	}
	
	public Model loadUntexturedModel(String fileName) {
		readFileData(fileName);
		vertices = new float[faceList.size() * 3 * 3];
		normals = new float[faceList.size() * 3 * 3];
		for(int i = 0; i < faceList.size(); i++) {
			String[] face = faceList.get(i).split(" ");
			
			writeUntexturedVertexData(face[0].split("/"), i * 3);
			writeUntexturedVertexData(face[1].split("/"), i * 3 + 1);
			writeUntexturedVertexData(face[2].split("/"), i * 3 + 2);
		}
		
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		loadDataToVBO(vertices, 0, 3);
		loadDataToVBO(normals, 1, 3);
		GL30.glBindVertexArray(0);
		Model model = new Model(vaoID, faceList.size() * 3);
		
		return model;
	}
	
	public void writeUntexturedVertexData(String[] values, int i) {
		int vertexIndex = Integer.parseInt(values[0]) - 1;
		int normalIndex = Integer.parseInt(values[2]) - 1;
		
		Vector3f vertex = vertexArray.get(vertexIndex);
		vertices[i * 3] = vertex.x;
		vertices[i * 3 + 1] = vertex.y;
		vertices[i * 3 + 2] = vertex.z;
		
		Vector3f normal = normalArray.get(normalIndex);
		normals[i * 3] = normal.x;
		normals[i * 3 + 1] = normal.y;
		normals[i * 3 + 2] = normal.z;
	}
	
	public int loadDataToVBO(float[] data, int location, int dimension) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(location, dimension, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	public void readFileData(String fileName) {
		File file = new File(fileName);
		vertexArray.clear();
		normalArray.clear();
		textureArray.clear();
		faceList.clear();
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNext()) {
				String line = scanner.nextLine();
				if(line.startsWith("v ")) {
					String[] values = line.split(" ");
					vertexArray.add(new Vector3f(Float.parseFloat(values[1]), Float.parseFloat(values[2]), Float.parseFloat(values[3])));
				}else if(line.startsWith("vn ")) {
					String[] values = line.split(" ");
					normalArray.add(new Vector3f(Float.parseFloat(values[1]), Float.parseFloat(values[2]), Float.parseFloat(values[3])));
				}else if(line.startsWith("t ")) {
					String[] values = line.split(" ");
					textureArray.add(new Vector2f(Float.parseFloat(values[1]), Float.parseFloat(values[2])));
				}else if(line.startsWith("f ")) {
					faceList.add(line.substring(2, line.length()));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Failed to read file: " + fileName);
		}
		System.out.println("Successfully read obj file: " + fileName);
	}

}
