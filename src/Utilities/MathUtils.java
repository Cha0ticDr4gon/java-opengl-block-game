package Utilities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Physics.Matrix3;

public class MathUtils {

	public static Matrix4f createProjectionMatrix(float width, float height, float NEAR_PLANE, float FAR_PLANE, float FOV) {
		float aspectRatio = width / height;
		System.out.println(width + " " + height + " " + aspectRatio);
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * FAR_PLANE * NEAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		
		return projectionMatrix;
	}
	
	public static Matrix4f createViewMatrix(Vector3f translation, float xRot, float yRot, float zRot) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(-xRot), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(-yRot), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(-zRot), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.translate(new Vector3f(-translation.x, -translation.y, -translation.z), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f position, Vector3f rotation, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(position, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f position, float xRot, float yRot, float zRot, float xScale, float yScale, float zScale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(position, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(zRot), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(yRot), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(xRot), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.scale(new Vector3f(xScale, yScale, zScale), matrix, matrix);
		return matrix;
	}
	
	public static void rotateVector (Vector3f vector, float xAngle, float yAngle, float zAngle) {
		double xRadians = Math.toRadians(xAngle);
		double yRadians = Math.toRadians(yAngle);
		double zRadians = Math.toRadians(zAngle);
		Matrix3 xRot = new Matrix3(new double[] {1, 0, 0, 0, Math.cos(xRadians), -Math.sin(xRadians), 0, Math.sin(xRadians), Math.cos(xRadians)});
		Matrix3 yRot = new Matrix3(new double[] {Math.cos(yRadians), 0, Math.sin(yRadians), 0, 1, 0, -Math.sin(yRadians), 0, Math.cos(yRadians)});
		Matrix3 zRot = new Matrix3(new double[] {Math.cos(zRadians), -Math.sin(zRadians), 0, Math.sin(zRadians), Math.cos(zRadians), 0, 0, 0, 1});
		Matrix3 transformation = Matrix3.multiply(zRot, Matrix3.multiply(yRot, xRot));
		transformation.rotate(vector);
	}
	
	public static Matrix4f createGuiTransformationMatrix(Vector2f position, float rotation, float xScale, float yScale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(new Vector3f(position.x, position.y, 0.0f), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(xScale, yScale, 1.0f), matrix, matrix);
		return matrix;
	}
}
