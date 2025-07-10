package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderUtils {
	
	static HashMap <Integer, Integer[]> programMap = new HashMap<Integer, Integer[]>();
	
	public static int loadProgram(String vertexFile, String fragmentFile, String programName) {
		int programID = GL20.glCreateProgram();
		int vertexShader = loadShader(vertexFile, GL20.GL_VERTEX_SHADER, programName + " - vertexShader");
		int fragmentShader = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER, programName + " - fragmentShader");
		GL20.glAttachShader(programID, vertexShader);
		GL20.glAttachShader(programID, fragmentShader);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		programMap.put(programID, new Integer[] {vertexShader, fragmentShader});
		System.out.println("Successfully loaded: " + programName);
		return programID;
	}
	
	private static int loadShader(String fileName, int shaderType, String shaderName) {
		int shaderID = GL20.glCreateShader(shaderType);
		GL20.glShaderSource(shaderID, loadSource(fileName));
		GL20.glCompileShader(shaderID);
		int status = GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS);
		if(status == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.out.println("Failed to compile shader: " + shaderName);
			System.exit(-1);
		}
		
		System.out.println("Successfully compiled shader: " + shaderName);
		return shaderID;
	}
	
	private static String loadSource(String fileName) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			String line = reader.readLine();
			while(line != null) {
				shaderSource.append(line).append("\n");
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Cound not find source: " + fileName);
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not read source: " + fileName);
			System.exit(-1);
		}
		return shaderSource.toString();
	}
	
	public static void destroy(int programID) {
		for(int shaderID : programMap.get(programID)) {
			GL20.glDetachShader(programID, shaderID);
			GL20.glDeleteShader(shaderID);
		}
		GL20.glDeleteProgram(programID);
		programMap.remove(programID);
		System.out.println("Removed program: " + programID);
	}
	
	public static void destroyAll() {
		for(int programID : programMap.keySet()) {
			destroy(programID);
		}
		System.out.println("Removed all programs");
	}

}
