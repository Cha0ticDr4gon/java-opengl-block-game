package Physics;

import org.lwjgl.util.vector.Vector3f;

public class Matrix3 {
	
	double[] values = new double[9];
	
	private double tempX = 0;
	private double tempY = 0;
	private double tempZ = 0;

	public Matrix3(double[] values) {
		this.values = values;
	}
	
	public void rotate(Vector3f vector) {
		tempX = vector.x;
		tempY = vector.y;
		tempZ = vector.z;
		vector.x = (float) (values[0] * tempX + values[1] * tempY + values[2] * tempZ);
		vector.y = (float) (values[3] * tempX + values[4] * tempY + values[5] * tempZ);
		vector.z = (float) (values[6] * tempX + values[7] * tempY + values[8] * tempZ);
	}
	
	public void transform(Vector3 vector, Vector3 position, Vector3 scale, Vector3 destination) {
		tempX = vector.x * scale.x;
		tempY = vector.y * scale.y;
		tempZ = vector.z * scale.z;
		destination.x = values[0] * tempX + values[1] * tempY + values[2] * tempZ + position.x;
		destination.y = values[3] * tempX + values[4] * tempY + values[5] * tempZ + position.y;
		destination.z = values[6] * tempX + values[7] * tempY + values[8] * tempZ + position.z;
	}
	
	public static Matrix3 multiply(Matrix3 a, Matrix3 b) {
		double[] newValues = new double[9];
		
		newValues[0] = a.values[0] * b.values[0] + a.values[1] * b.values[3] + a.values[2] * b.values[6];
		newValues[1] = a.values[0] * b.values[1] + a.values[1] * b.values[4] + a.values[2] * b.values[7];
		newValues[2] = a.values[0] * b.values[2] + a.values[1] * b.values[5] + a.values[2] * b.values[8];
		
		newValues[3] = a.values[3] * b.values[0] + a.values[4] * b.values[3] + a.values[5] * b.values[6];
		newValues[4] = a.values[3] * b.values[1] + a.values[4] * b.values[4] + a.values[5] * b.values[7];
		newValues[5] = a.values[3] * b.values[2] + a.values[4] * b.values[5] + a.values[5] * b.values[8];
		
		newValues[6] = a.values[6] * b.values[0] + a.values[7] * b.values[3] + a.values[8] * b.values[6];
		newValues[7] = a.values[6] * b.values[1] + a.values[7] * b.values[4] + a.values[8] * b.values[7];
		newValues[8] = a.values[6] * b.values[2] + a.values[7] * b.values[5] + a.values[8] * b.values[8];
		
		Matrix3 result = new Matrix3(newValues);
		return result;
	}
}
