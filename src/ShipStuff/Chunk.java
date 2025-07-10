package ShipStuff;

import org.lwjgl.util.vector.Vector3f;

public class Chunk {
	
	public static final int GRID_SIZE_X = 16;
	public static final int GRID_SIZE_Y = 16;
	public static final int GRID_SIZE_Z = 16;
	
	public static final float CELL_SIZE_X = 1.0f;
	public static final float CELL_SIZE_Y = 1.0f;
	public static final float CELL_SIZE_Z = 1.0f;
	
	public static final float CHUNK_LENGTH_X = CELL_SIZE_X * GRID_SIZE_X;
	public static final float CHUNK_LENGTH_Y = CELL_SIZE_Y * GRID_SIZE_Y;
	public static final float CHUNK_LENGTH_Z = CELL_SIZE_Z * GRID_SIZE_Z;
	
	int[] grid = new int[GRID_SIZE_X * GRID_SIZE_Y * GRID_SIZE_Z];
	
	public Vector3f position;
	
	Chunk topChunk;
	Chunk bottomChunk;
	Chunk rightChunk;
	Chunk leftChunk;
	Chunk frontChunk;
	Chunk backChunk;
	
	public int x;
	public int y;
	public int z;
	
	boolean visited = false;
	
	Chunk(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		position = new Vector3f(x * CHUNK_LENGTH_X, y * CHUNK_LENGTH_Y, z * CHUNK_LENGTH_Z);
	}
	
	public int getCellValue(int x, int y, int z) {
		return grid[x + y * GRID_SIZE_X + z * GRID_SIZE_X * GRID_SIZE_Y];
	}
	
	public void setCellValue(int x, int y, int z, int value) {
		grid[x + y * GRID_SIZE_X + z * GRID_SIZE_X * GRID_SIZE_Y] = value;
	}

}
