package GUI;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import Utilities.ShaderUtils;

public class GUIRenderer {
	
	String guiVertexFile = "src/ShaderPrograms/guiVertexShader1.glsl";
	String guiFragmentFile = "src/ShaderPrograms/guiFragmentShader1.glsl";
	
	int guiProgram;
	
	int location_transformationMatrix;
	int location_projectionMatrix;
	
	ArrayList <GUIEntity> guiEntities = new ArrayList<GUIEntity>();
	
	FloatBuffer matrixBuffer = FloatBuffer.allocate(16);
	
	Matrix4f projectionMatrix;
	
	public GUIRenderer() {
		loadGUIProgram();
		projectionMatrix = createGuiProjectionMatrix();
	}
	
	public void render() {
		GL20.glUseProgram(guiProgram);
		GL20.glUniformMatrix4(location_projectionMatrix, false, getMatrixBuffer(projectionMatrix));
		for(GUIEntity entity : guiEntities) {
			GL20.glUniformMatrix4(location_transformationMatrix, false, getMatrixBuffer(entity.getTransform()));
			
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, entity.model.vertexCount);
		}
	}
	
	public FloatBuffer getMatrixBuffer(Matrix4f matrix) {
		matrixBuffer.clear();
		matrix.load(matrixBuffer);
		matrixBuffer.flip();
		return matrixBuffer;
	}
	
	public void loadGUIProgram() {
		guiProgram = ShaderUtils.loadProgram(guiVertexFile, guiFragmentFile, "GUI Program");
		
		location_transformationMatrix = GL20.glGetUniformLocation(guiProgram, "transformationMatrix");
		location_projectionMatrix = GL20.glGetUniformLocation(guiProgram, "projectionMatrix");
	}
	
	//Creates projection matrix for GUIs... come back and fix this later...
	public Matrix4f createGuiProjectionMatrix() {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		return matrix;
	}

}
