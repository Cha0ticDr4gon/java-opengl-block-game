package ShipStuff;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.Model;

public class Block extends Entity{
	
	public int blockID;
	int xLength;
	int yLength;
	int zLength;
	
	public Block(Model model, Vector3f position, int blockID, int xLength, int yLength, int zLength) {
		super(model, position);
		this.blockID = blockID;
		this.xLength = xLength;
		this.yLength = yLength;
		this.zLength = zLength;
		scale.x = xLength * Chunk.CELL_SIZE_X / 2.0f;
		scale.y = yLength * Chunk.CELL_SIZE_Y / 2.0f;
		scale.z = zLength * Chunk.CELL_SIZE_Z / 2.0f;
	}

}
