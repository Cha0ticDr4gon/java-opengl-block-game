package Engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.BoxWireframe;
import Entities.Camera;
import Entities.ColliderMesh;
import Entities.Entity;
import Entities.Light;
import Entities.PlaneWireframe;
import Entities.WireframeEntity;
import Models.Model;
import Physics.BoxCollider;
import Physics.CollisionEngine;
import Physics.Vector3;
import ShipStuff.Chunk;
import ShipStuff.RaycastPackage;
import ShipStuff.ShipEntity;
import Utilities.MathUtils;
import Utilities.ObjFileLoader;
import Utilities.ShaderUtils;

public class LegoEngine1 {
	
	//Important Constant Stuff
	final int WIDTH = 1080;
	final int HEIGHT = 720;
	
	final int FPS = 60;
	
	//Frame rate stuff
	double currentTime = System.nanoTime();
	double lastTime;
	double passedTime;
	double deltaTime;
	double secondTime;
	double secondCap = 1000000000d;
	
	int framesPerSecond = 0;
	
	//Render Shader stuff
	String renderVertexFile = "src/ShaderPrograms/renderVertexShader1.glsl";
	String renderFragmentFile = "src/ShaderPrograms/renderFragmentShader1.glsl";
	
	int renderVertexShader;
	int renderFragmentShader;
	int renderProgram;
	
	int render_location_transformationMatrix;
	int render_location_projectionMatrix;
	int render_location_viewMatrix;
	
	int render_location_lightPosition;
	int render_location_lightColor;
	int render_location_ambient;
	int render_location_albedo;
	
	//Collider mesh shader stuff
	String colliderVertexFile = "src/ShaderPrograms/colliderVertexShader1.glsl";
	String colliderFragmentFile = "src/ShaderPrograms/colliderFragmentShader1.glsl";
	
	int colliderVertexShader;
	int colliderFragmentShader;
	int colliderProgram;
	
	int collider_location_transformationMatrix;
	int collider_location_projectionMatrix;
	int collider_location_viewMatrix;
	
	int collider_location_color;
	
	//Quad stuff
	float[] quadPositions = {-1.0f, -1.0f, 0.0f, 
							 1.0f, -1.0f, 0.0f, 
							 -1.0f, 1.0f, 0.0f, 
							 
							 -1.0f, 1.0f, 0.0f, 
							 1.0f, -1.0f, 0.0f, 
							 1.0f, 1.0f, 0.0f};
	
	float[] quadColors = {0.0f, 0.0f, 1.0f, 
						  0.0f, 0.0f, 1.0f, 
						  0.0f, 0.0f, 1.0f, 
						  
						  0.0f, 0.0f, 1.0f, 
						  0.0f, 0.0f, 1.0f, 
						  0.0f, 0.0f, 1.0f};
	
	//Matrix stuff
	FloatBuffer matrixBuffer;
	
	Matrix4f projectionMatrix;
	Matrix4f viewMatrix;
	
	//Entity stuff
	ArrayList <Entity> entities = new ArrayList<Entity>();
	
	ObjFileLoader objFileLoader = new ObjFileLoader();
	
	Camera camera = new Camera(new Vector3f(0, 0, 10), new Vector3f(0, 0, 0));
	
	ShipEntity ship1;
	
	//Light Stuff
	Light light1 = new Light(new Vector3f(8, 10, 6), new Vector3f(1.0f, 1.0f, 1.0f));
	
	//Collision stuff
	CollisionEngine collisionEngine = new CollisionEngine();
	
	ArrayList <ColliderMesh> colliders = new ArrayList<ColliderMesh>();
	
	//Mesh rendering stuff
	ArrayList <WireframeEntity> meshes = new ArrayList<WireframeEntity>();
	
	//Keyboard shortcut stuff
	boolean escaped = false;
	boolean click1 = false;
	
	boolean mouseLeftClick = false;
	boolean mouseRightClick = false;
	
	LegoEngine1() {
		ContextAttribs attribs = new ContextAttribs(3, 3);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Lego Builder Thing");
			
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		
		loadRenderProgram();
		loadColliderProgram();
		
		
		
		Model quadModel = new Model(loadMeshToVAO(quadPositions, quadColors), 6);
		Entity quad = new Entity(quadModel, new Vector3f(0, 0, 0));
		//entities.add(quad);
		
		Model cubeModel = objFileLoader.loadUntexturedModel("res/UntexturedCube.obj");
		Entity cube1 = new Entity(cubeModel, new Vector3f(2, -5, 0), new Vector3f(0, 0, 0), new Vector3f(5, 0.25f, 5));
		cube1.setColor(0.5f, 0.5f, 1.0f);
		entities.add(cube1);
		BoxCollider cubeCollider1 = new BoxCollider(new Vector3(2, -5, 0), new Vector3(0, 0, 0), new Vector3(5, 0.25, 5));
		ColliderMesh colliderMesh1 = new ColliderMesh(cubeCollider1);
		colliders.add(colliderMesh1);
		meshes.add(colliderMesh1);
		
		Entity cube2 = new Entity(cubeModel, new Vector3f(0, 3, 0), new Vector3f(45, 0, 45), new Vector3f(1, 1, 1));
		cube2.setColor(1.0f, 0.2f, 0.2f);
		entities.add(cube2);
		BoxCollider cubeCollider2 = new BoxCollider(new Vector3(0, 3, 0), new Vector3(45, 0, 45), new Vector3(1, 1, 1));
		ColliderMesh colliderMesh2 = new ColliderMesh(cubeCollider2);
		colliders.add(colliderMesh2);
		meshes.add(colliderMesh2);
		
		
		
		ship1 = new ShipEntity(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), cubeModel);
		ArrayList <Chunk> chunks = ship1.getAllChunks();
		for(Chunk chunk : chunks) meshes.add(new BoxWireframe(new Vector3f(chunk.position.x + Chunk.CHUNK_LENGTH_X / 2.0f, chunk.position.y + Chunk.CHUNK_LENGTH_Y / 2.0f, chunk.position.z + Chunk.CHUNK_LENGTH_Z / 2.0f), new Vector3f(Chunk.CHUNK_LENGTH_X / 2.0f, Chunk.CHUNK_LENGTH_Y / 2.0f, Chunk.CHUNK_LENGTH_Z / 2.0f), new Vector3f(0.1f, 0.1f, 1.0f)));
		BoxWireframe cellMesh = new BoxWireframe(new Vector3f(0, 0, 0), new Vector3f(Chunk.CELL_SIZE_X / 2.0f, Chunk.CELL_SIZE_Y / 2.0f, Chunk.CELL_SIZE_Z / 2.0f), new Vector3f(1.0f, 1.0f, 0.0f));
		PlaneWireframe faceMesh = new PlaneWireframe(new Vector3f(0, 0, 0), new Vector3f(Chunk.CELL_SIZE_X / 2.0f, 1, Chunk.CELL_SIZE_Z / 2.0f), new Vector3f(1.0f, 0.0f, 0.0f));
		meshes.add(faceMesh);
		
		
		
		projectionMatrix = MathUtils.createProjectionMatrix(WIDTH, HEIGHT, 0.1f, 1000.0f, 90.0f);
		viewMatrix = MathUtils.createViewMatrix(new Vector3f(0, 0, -10), 0, 0, 0);
		
		
		
		Mouse.setCursorPosition(WIDTH / 2, HEIGHT / 2);
		
		while(!Display.isCloseRequested()) {
			//Keeps track of frame count
			framesPerSecond++;
			lastTime = currentTime;
			currentTime = System.nanoTime();
			passedTime = currentTime - lastTime;
			secondTime += passedTime;
			deltaTime = passedTime / 1000000000d;
			while(secondTime > secondCap) {
				System.out.println("FPS: " + framesPerSecond);
				secondTime = 0;
				framesPerSecond = 0;
			}
			
			//Mouse update stuff
			if(click1 == false && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Mouse.setCursorPosition(WIDTH / 2, HEIGHT / 2);
				escaped = !escaped;
			}
			click1 = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
			
			if(escaped) {
				float xDiff = Mouse.getX() - (WIDTH / 2);
				float yDiff = Mouse.getY() - (HEIGHT / 2);
				camera.rotation.x += yDiff / 4.0f;
				camera.rotation.x = Math.min(90.0f, Math.max(-90.0f, camera.rotation.x));
				camera.rotation.y -= xDiff / 4.0f;
				Mouse.setCursorPosition(WIDTH / 2, HEIGHT / 2);
			}
			
			//Camera movement
			camera.update((float) deltaTime);
			
			//Raycasting test
			Vector3f cameraDirection = new Vector3f(0, 0, -1);
			MathUtils.rotateVector(cameraDirection, camera.rotation.x, camera.rotation.y, camera.rotation.z);
			Chunk chunk1 = ship1.raycastChunk(camera.position, cameraDirection);
			if(chunk1 != null) {
				//System.out.println("Ray chunk found! [" + chunk1.x + ", " + chunk1.y + ", " + chunk1.z + "]");
				//System.out.println("[" + ship1.blockDirection.x + ", " + ship1.blockDirection.y + ", " + ship1.blockDirection.z + "]");
				if(ship1.blockDirection.y == 1) {
					faceMesh.position.set(chunk1.position.x + ship1.xBlock * Chunk.CELL_SIZE_X + Chunk.CELL_SIZE_X / 2.0f, chunk1.position.y + ship1.yBlock * Chunk.CELL_SIZE_Y + Chunk.CELL_SIZE_Y, chunk1.position.z + ship1.zBlock * Chunk.CELL_SIZE_Z + Chunk.CELL_SIZE_Z / 2.0f);
					faceMesh.rotation.set(0, 0, 0);
					faceMesh.scale.set(Chunk.CELL_SIZE_X / 2.0f, 0.0f, Chunk.CELL_SIZE_Z / 2.0f);
				}else if(ship1.blockDirection.y == -1) {
					faceMesh.position.set(chunk1.position.x + ship1.xBlock * Chunk.CELL_SIZE_X + Chunk.CELL_SIZE_X / 2.0f, chunk1.position.y + ship1.yBlock * Chunk.CELL_SIZE_Y, chunk1.position.z + ship1.zBlock * Chunk.CELL_SIZE_Z + Chunk.CELL_SIZE_Z / 2.0f);
					faceMesh.rotation.set(180, 0, 0);
					faceMesh.scale.set(Chunk.CELL_SIZE_X / 2.0f, 0.0f, Chunk.CELL_SIZE_Z / 2.0f);
				}else if(ship1.blockDirection.z == 1) {
					faceMesh.position.set(chunk1.position.x + ship1.xBlock * Chunk.CELL_SIZE_X + Chunk.CELL_SIZE_X / 2.0f, chunk1.position.y + ship1.yBlock * Chunk.CELL_SIZE_Y + Chunk.CELL_SIZE_Y / 2.0f, chunk1.position.z + ship1.zBlock * Chunk.CELL_SIZE_Z + Chunk.CELL_SIZE_Z);
					faceMesh.rotation.set(90, 0, 0);
					faceMesh.scale.set(Chunk.CELL_SIZE_X / 2.0f, 0.0f, Chunk.CELL_SIZE_Z / 2.0f);
				}else if(ship1.blockDirection.z == -1) {
					faceMesh.position.set(chunk1.position.x + ship1.xBlock * Chunk.CELL_SIZE_X + Chunk.CELL_SIZE_X / 2.0f, chunk1.position.y + ship1.yBlock * Chunk.CELL_SIZE_Y + Chunk.CELL_SIZE_Y / 2.0f, chunk1.position.z + ship1.zBlock * Chunk.CELL_SIZE_Z);
					faceMesh.rotation.set(90, 180, 0);
					faceMesh.scale.set(Chunk.CELL_SIZE_X / 2.0f, 0.0f, Chunk.CELL_SIZE_Z / 2.0f);
				}else if(ship1.blockDirection.x == 1) {
					faceMesh.position.set(chunk1.position.x + ship1.xBlock * Chunk.CELL_SIZE_X + Chunk.CELL_SIZE_X, chunk1.position.y + ship1.yBlock * Chunk.CELL_SIZE_Y + Chunk.CELL_SIZE_Y / 2.0f, chunk1.position.z + ship1.zBlock * Chunk.CELL_SIZE_Z + Chunk.CELL_SIZE_Z / 2.0f);
					faceMesh.rotation.set(90, 90, 0);
					faceMesh.scale.set(Chunk.CELL_SIZE_X / 2.0f, 0.0f, Chunk.CELL_SIZE_Z / 2.0f);
				}else if(ship1.blockDirection.x == -1) {
					faceMesh.position.set(chunk1.position.x + ship1.xBlock * Chunk.CELL_SIZE_X, chunk1.position.y + ship1.yBlock * Chunk.CELL_SIZE_Y + Chunk.CELL_SIZE_Y / 2.0f, chunk1.position.z + ship1.zBlock * Chunk.CELL_SIZE_Z + Chunk.CELL_SIZE_Z / 2.0f);
					faceMesh.rotation.set(90, 270, 0);
					faceMesh.scale.set(Chunk.CELL_SIZE_X / 2.0f, 0.0f, Chunk.CELL_SIZE_Z / 2.0f);
				}
				
				if(Mouse.isButtonDown(0) && !mouseLeftClick) {
					System.out.println("Mouse button was pressed: " + Mouse.getButtonName(0));
					//ship1.setBlockValue(ship1.xBlock + chunk1.x * Chunk.GRID_SIZE_X + (int) ship1.blockDirection.x, ship1.yBlock  + chunk1.y * Chunk.GRID_SIZE_Y + (int) ship1.blockDirection.y, ship1.zBlock + chunk1.z * Chunk.GRID_SIZE_Z + (int) ship1.blockDirection.z, 1);
					ship1.placeBlock(ship1.xBlock + chunk1.x * Chunk.GRID_SIZE_X + (int) ship1.blockDirection.x, ship1.yBlock  + chunk1.y * Chunk.GRID_SIZE_Y + (int) ship1.blockDirection.y, ship1.zBlock + chunk1.z * Chunk.GRID_SIZE_Z + (int) ship1.blockDirection.z);
				}
				mouseLeftClick = Mouse.isButtonDown(0);
				
				if(Mouse.isButtonDown(1) && !mouseRightClick) {
					System.out.println("Mouse button was pressed: " + Mouse.getButtonName(1));
					//ship1.setBlockValue(ship1.xBlock + chunk1.x * Chunk.GRID_SIZE_X, ship1.yBlock  + chunk1.y * Chunk.GRID_SIZE_Y, ship1.zBlock + chunk1.z * Chunk.GRID_SIZE_Z, 0);
					ship1.removeBlock(ship1.xBlock + chunk1.x * Chunk.GRID_SIZE_X, ship1.yBlock  + chunk1.y * Chunk.GRID_SIZE_Y, ship1.zBlock + chunk1.z * Chunk.GRID_SIZE_Z);
				}
				mouseRightClick = Mouse.isButtonDown(1);
			}
			
			
			//Collision test
			double startTime = System.nanoTime();
			Vector3f velocity = getControlVector(false);
			float speed = 1.0f;
			velocity.x *= speed;
			velocity.y *= speed;
			velocity.z *= speed;
			
			cubeCollider2.position.x += velocity.x * deltaTime;
			cubeCollider2.position.y += velocity.y * deltaTime;
			cubeCollider2.position.z += velocity.z * deltaTime;
			
			Vector3f angularVelocity = new Vector3f(0, 0, 90);
			
			cubeCollider2.rotation.x += angularVelocity.x * deltaTime;
			cubeCollider2.rotation.y += angularVelocity.y * deltaTime;
			cubeCollider2.rotation.z += angularVelocity.z * deltaTime;
			
			cubeCollider2.transform();
			Vector3 displacement = collisionEngine.OrientedBoxToOrientedBoxCollision2(cubeCollider2, cubeCollider1);
			if(displacement != null) {
				//System.out.println("Collision found!");
				cubeCollider2.position.x += displacement.x * 1.02;
				cubeCollider2.position.y += displacement.y * 1.02;
				cubeCollider2.position.z += displacement.z * 1.02;
				cubeCollider2.transform();
			}
			/*
			displacement = collisionEngine.OrientedBoxToOrientedBoxCollision(cubeCollider1, cubeCollider2);
			if(displacement != null) {
				System.out.println("Collision found!");
				cubeCollider2.position.x -= displacement.x * 1.01;
				cubeCollider2.position.y -= displacement.y * 1.01;
				cubeCollider2.position.z -= displacement.z * 1.01;
				cubeCollider2.transform();
			}
			*/
			cubeCollider2.applyTransform();
			cube2.position.x = (float) cubeCollider2.position.x;
			cube2.position.y = (float) cubeCollider2.position.y;
			cube2.position.z = (float) cubeCollider2.position.z;
			
			cube2.rotation.x = (float) cubeCollider2.rotation.x;
			cube2.rotation.y = (float) cubeCollider2.rotation.y;
			cube2.rotation.z = (float) cubeCollider2.rotation.z;
			/*
			if(velocity.length() > 0) {
				System.out.println("Cube Position: [" + cube2.position.x + ", " + cube2.position.y + ", " + cube2.position.z + "]");
			}
			*/
			double endTime = System.nanoTime();
			double passedTime = endTime - startTime;
			System.out.println("Time: " + (passedTime / 1000000));
			//RENDER STAGE STARTS HERE
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.0f,  0.0f, 0.0f, 1.0f);
			
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
			
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			GL20.glUseProgram(renderProgram);
			
			GL20.glUniformMatrix4(render_location_projectionMatrix, false, getMatrixBuffer(projectionMatrix));
			GL20.glUniformMatrix4(render_location_viewMatrix, false, getMatrixBuffer(camera.getTransform()));
			
			GL20.glUniform3f(render_location_lightPosition, light1.position.x, light1.position.y, light1.position.z);
			GL20.glUniform3f(render_location_lightColor, light1.color.x, light1.color.y, light1.color.z);
			GL20.glUniform1f(render_location_ambient, 0.2f);
			
			for(Entity entity : entities) {
				GL20.glUniformMatrix4(render_location_transformationMatrix, false, getMatrixBuffer(entity.getTransform()));
				
				GL20.glUniform3f(render_location_albedo, entity.color.x, entity.color.y, entity.color.z);
				
				GL30.glBindVertexArray(entity.model.vaoID);
				GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
				
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, entity.model.vertexCount);
				
				GL20.glDisableVertexAttribArray(0);
				GL20.glDisableVertexAttribArray(1);
				GL30.glBindVertexArray(0);
			}
			
			for(Integer key : ship1.entities.keySet()) {
				Entity entity = ship1.entities.get(key);
				GL20.glUniformMatrix4(render_location_transformationMatrix, false, getMatrixBuffer(entity.getTransform()));
				
				GL20.glUniform3f(render_location_albedo, entity.color.x, entity.color.y, entity.color.z);
				
				GL30.glBindVertexArray(entity.model.vaoID);
				GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
				
				GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, entity.model.vertexCount);
				
				GL20.glDisableVertexAttribArray(0);
				GL20.glDisableVertexAttribArray(1);
				GL30.glBindVertexArray(0);
			}
			
			GL20.glUseProgram(0);
			//RENDER STAGE ENDS HERE
			
			for(ColliderMesh mesh : colliders) mesh.updatePosition();
			
			//MESH RENDERING STAGE STARTS HERE
			GL20.glUseProgram(colliderProgram);
			
			GL20.glUniformMatrix4(collider_location_projectionMatrix, false, getMatrixBuffer(projectionMatrix));
			GL20.glUniformMatrix4(collider_location_viewMatrix, false, getMatrixBuffer(camera.getTransform()));
			
			for(WireframeEntity mesh : meshes) {
				GL20.glUniformMatrix4(collider_location_transformationMatrix, false, getMatrixBuffer(mesh.getTransform()));
				GL20.glUniform3f(collider_location_color, mesh.color.x, mesh.color.y, mesh.color.z);
				
				GL30.glBindVertexArray(mesh.model.vaoID);
				GL20.glEnableVertexAttribArray(0);
				
				GL11.glLineWidth(2.0f);
				GL11.glDrawArrays(GL11.GL_LINES, 0, mesh.model.vertexCount);
				
				GL20.glDisableVertexAttribArray(0);
				GL30.glBindVertexArray(0);
			}
			
			/*
			//Draws all intersected cells in chunks from ship
			for(int i = 0; i < ship1.positionCount; i++) {
				GL20.glUniformMatrix4(collider_location_transformationMatrix, false, getMatrixBuffer(MathUtils.createTransformationMatrix(ship1.blockPositions.get(i), cellMesh.rotation, cellMesh.scale)));
				GL20.glUniform3f(collider_location_color, cellMesh.color.x, cellMesh.color.y, cellMesh.color.z);
				
				GL30.glBindVertexArray(cellMesh.model.vaoID);
				GL20.glEnableVertexAttribArray(0);
				
				GL11.glDrawArrays(GL11.GL_LINES, 0, cellMesh.model.vertexCount);
				
				GL20.glDisableVertexAttribArray(0);
				GL30.glBindVertexArray(0);
			}
			*/
			GL20.glUseProgram(0);
			//MESH RENDERING STAGE ENDS HERE
			
			//Synchronizes and updates display
			Display.sync(FPS);
			Display.update();
		}
		
		Display.destroy();
	}
	
	public Vector3f getControlVector(boolean print) {
		Vector3f direction = new Vector3f(0, 0, 0);
		if(Keyboard.isKeyDown(Keyboard.KEY_I)) direction.z -= 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_K)) direction.z += 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_J)) direction.x -= 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_L)) direction.x += 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_U)) direction.y -= 1;
		if(Keyboard.isKeyDown(Keyboard.KEY_O)) direction.y += 1;
		float length = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z);
		if(length > 0) {
			direction.x /= length;
			direction.y /= length;
			direction.z /= length;
		}
		
		if(print) {
			System.out.println("Position: " + direction.x + ", " + direction.y + ", " + direction.z);
		}
		
		return direction;
	}
	
	public FloatBuffer getMatrixBuffer(Matrix4f matrix) {
		matrixBuffer = BufferUtils.createFloatBuffer(16);
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		return matrixBuffer;
	}
	
	public int loadMeshToVAO(float[] positions, float[] normals) {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		loadDataToVBO(positions, 0, 3);
		loadDataToVBO(normals, 1, 3);
		
		GL30.glBindVertexArray(0);
		return vaoID;
	}
	
	public int loadDataToVBO(float[] data, int location, int dimension) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(location, dimension,  GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	//Shader program loader stuff
	public void loadRenderProgram() {
		System.out.println("Loading render program");
		/*
		renderProgram = GL20.glCreateProgram();
		renderVertexShader = loadShader(renderVertexFile, GL20.GL_VERTEX_SHADER, "renderVertexShader");
		renderFragmentShader = loadShader(renderFragmentFile, GL20.GL_FRAGMENT_SHADER, "renderFragmentShader");
		
		GL20.glAttachShader(renderProgram, renderVertexShader);
		GL20.glAttachShader(renderProgram, renderFragmentShader);
		
		GL20.glLinkProgram(renderProgram);
		GL20.glValidateProgram(renderProgram);
		int linkStatus = GL20.glGetProgrami(renderProgram, GL20.GL_LINK_STATUS);
		if(linkStatus == GL11.GL_FALSE) {
			System.out.println("Failed to link render program!");
			System.exit(-1);
		}
		*/
		renderProgram = ShaderUtils.loadProgram(renderVertexFile, renderFragmentFile, "Render Program");
		
		render_location_transformationMatrix = GL20.glGetUniformLocation(renderProgram, "transformationMatrix");
		render_location_projectionMatrix = GL20.glGetUniformLocation(renderProgram, "projectionMatrix");
		render_location_viewMatrix = GL20.glGetUniformLocation(renderProgram, "viewMatrix");
		
		render_location_lightColor = GL20.glGetUniformLocation(renderProgram, "lightColor");
		render_location_lightPosition = GL20.glGetUniformLocation(renderProgram, "lightPosition");
		render_location_ambient = GL20.glGetUniformLocation(renderProgram, "ambient");
		
		render_location_albedo = GL20.glGetUniformLocation(renderProgram, "albedo");
		
		System.out.println("Successfully loaded render program");
	}
	
	public void loadColliderProgram() {
		System.out.println("Loading collider program");
		colliderProgram = GL20.glCreateProgram();
		colliderVertexShader = loadShader(colliderVertexFile, GL20.GL_VERTEX_SHADER, "colliderVertexShader");
		colliderFragmentShader = loadShader(colliderFragmentFile, GL20.GL_FRAGMENT_SHADER, "colliderFragmentShader");
		
		GL20.glAttachShader(colliderProgram, colliderVertexShader);
		GL20.glAttachShader(colliderProgram, colliderFragmentShader);
		
		GL20.glLinkProgram(colliderProgram);
		GL20.glValidateProgram(colliderProgram);
		int linkStatus = GL20.glGetProgrami(colliderProgram, GL20.GL_LINK_STATUS);
		if(linkStatus == GL11.GL_FALSE) {
			System.out.println("Failed to link collider program!");
		}
		
		collider_location_transformationMatrix = GL20.glGetUniformLocation(colliderProgram, "transformationMatrix");
		collider_location_projectionMatrix = GL20.glGetUniformLocation(colliderProgram, "projectionMatrix");
		collider_location_viewMatrix = GL20.glGetUniformLocation(colliderProgram, "viewMatrix");
		
		collider_location_color = GL20.glGetUniformLocation(colliderProgram, "color");
		
		System.out.println("Successfully loaded collider program");
	}
	
	public int loadShader(String shaderFile, int shaderType, String shaderName) {
		StringBuilder shaderSource = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(shaderFile)));
			String line;
			while((line = reader.readLine()) != null) {
				shaderSource.append(line);
				shaderSource.append("\n");
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Failed to locate file: " + shaderFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to read file: " + shaderFile);
		}
		
		System.out.println("Successfully retrieved source: " + shaderFile);
		
		int shaderID = GL20.glCreateShader(shaderType);
		GL20.glShaderSource(shaderID, shaderSource.toString());
		GL20.glCompileShader(shaderID);
		int compileStatus = GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS);
		if(compileStatus == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.out.println("Could not compile shader: " + shaderName);
			System.exit(-1);
		}
		System.out.println("Compiled shader: " + shaderName);
		return shaderID;
	}
	
	public static void main(String[] args) {
		LegoEngine1 legoengine1 = new LegoEngine1();
	}

}
