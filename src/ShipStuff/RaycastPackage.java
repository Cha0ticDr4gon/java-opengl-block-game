package ShipStuff;

import org.lwjgl.util.vector.Vector3f;

public class RaycastPackage {
	
	public Chunk chunk;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public Vector3f direction;
	public int count;
	
	public RaycastPackage(Chunk chunk, int xCoord, int yCoord, int zCoord, Vector3f direction, int count) {
		this.chunk = chunk;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.direction = direction;
		this.count = count;
	}

}
