package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Utilities.MathUtils;

public class Camera {
	public Vector3f position;
	public Vector3f rotation;
	float speed = 4.0f;
	float rotSpeed = 90.0f;
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	public void update(float deltaTime) {
		//Updates position
		Vector3f direction = new Vector3f(0.0f, 0.0f, 0.0f);
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) direction.z -= 1.0f;
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) direction.z += 1.0f;
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) direction.x -= 1.0f;
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) direction.x += 1.0f;
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) direction.y += 1.0f;
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) direction.y -= 1.0f;
		
		//Normalizes the direction vector because Vector3f is stupid and doesn't know how to do it
		float length = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z);
		if(length > 0) {
			direction.x /= length;
			direction.y /= length;
			direction.z /= length;
		}
		
		//System.out.println(direction.x + " " + direction.y + " " + direction.z);
		
		//Updates rotation
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) rotation.x += rotSpeed * deltaTime;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) rotation.x -= rotSpeed * deltaTime;
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) rotation.y += rotSpeed * deltaTime;
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) rotation.y -= rotSpeed * deltaTime;
		
		float xRot = (float) Math.toRadians(-rotation.x);
		float yRot = (float) Math.toRadians(rotation.y);
		float tempY = (float) (Math.cos(xRot) * direction.y + Math.sin(xRot) * direction.z);
		float tempZ = (float) (-Math.sin(xRot) * direction.y + Math.cos(xRot) * direction.z);
		
		direction.y = tempY;
		direction.z = tempZ;
		
		float tempX = (float) (Math.cos(yRot) * direction.x + Math.sin(yRot) * direction.z);
		tempZ = (float) (-Math.sin(yRot) * direction.x + Math.cos(yRot) * direction.z);
		
		direction.x = tempX;
		direction.z = tempZ;
		
		//Applies movement
		position.x += direction.x * speed * deltaTime;
		position.y += direction.y * speed * deltaTime;
		position.z += direction.z * speed * deltaTime;
		/*
		if(direction.length() > 0) {
			System.out.println("Camera position: [" + position.x + ", " + position.y + ", " + position.z + "]");
		}
		*/
	}
	
	public Matrix4f getTransform() {
		return MathUtils.createViewMatrix(position, rotation.x, rotation.y, rotation.z);
	}
}
