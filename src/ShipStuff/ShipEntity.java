package ShipStuff;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Models.Model;

public class ShipEntity {
	
	public Chunk center;
	
	public Vector3f position;
	public Vector3f rotation;
	
	public ArrayList <Vector3f> blockPositions = new ArrayList<Vector3f>();
	public int positionCount = 0;
	
	public int xBlock = 0;
	public int yBlock = 0;
	public int zBlock = 0;
	public Vector3f blockDirection = new Vector3f(0, 0, 0);
	
	public HashMap <Integer, Entity> entities = new HashMap<Integer, Entity>();
	public int nextID = 1;
	
	Model model;
	
	public ShipEntity(Vector3f position, Vector3f rotation, Model model) {
		this.position = position;
		this.rotation = rotation;
		this.model = model;
		center = new Chunk(0, 0, 0);
		
		placeBlock(0, 0, 0);
		
		addChunkAtCoord(1, 0, 0);
		addChunkAtCoord(-1, 0, 0);
		addChunkAtCoord(0, 1, 0);
		addChunkAtCoord(0, -1, 0);
		addChunkAtCoord(0, 0, 1);
		addChunkAtCoord(0, 0, -1);
		
		System.out.println("[" + center.topChunk + ", " + center.bottomChunk + ", " + center.rightChunk + ", " + center.leftChunk + ", " + center.frontChunk + ", " + center.backChunk + "]");
		
		ArrayList <Chunk> chunks = getAllChunks();
		System.out.println("Number of chunks: " + chunks.size());
		
		//System.out.println("Infinity: " + Float.POSITIVE_INFINITY + ", " + Float.NEGATIVE_INFINITY);
	}
	
	public Chunk raycastChunk(Vector3f position, Vector3f direction) {
		int xCoord = (int) (position.x / Chunk.CHUNK_LENGTH_X);
		int yCoord = (int) (position.y / Chunk.CHUNK_LENGTH_Y);
		int zCoord = (int) (position.z / Chunk.CHUNK_LENGTH_Z);
		//Corrects for modulus
		if(position.x < 0) xCoord--;
		if(position.y < 0) yCoord--;
		if(position.z < 0) zCoord--;
		//Easiest way to ensure I don't get NaN when dividing
		if(direction.x == 0) direction.x = 0.000001f;
		if(direction.y == 0) direction.y = 0.000001f;
		if(direction.z == 0) direction.z = 0.000001f;
		
		//System.out.println("Chunk coord: [" + xCoord + ", " + yCoord + ", " + zCoord + "]");
		//System.out.println("Direction: [" + direction.x + ", " + direction.y + ", " + direction.z + "]");
		
		Chunk chunk = getChunkAtCoord(xCoord, yCoord, zCoord);
		
		Vector3f start = new Vector3f(position.x, position.y, position.z);
		
		if(chunk == null) {
			ArrayList <Chunk> chunks = getAllChunks();
			
			float t = Float.POSITIVE_INFINITY;
			
			for(Chunk chonk : chunks) {
				float tMin = Float.NEGATIVE_INFINITY;
				float tMax = Float.POSITIVE_INFINITY;
				
				float tx1 = (chonk.position.x - position.x) / direction.x;
				float tx2 = (chonk.position.x + Chunk.CHUNK_LENGTH_X - position.x) / direction.x;
				float ty1 = (chonk.position.y - position.y) / direction.y;
				float ty2 = (chonk.position.y + Chunk.CHUNK_LENGTH_Y - position.y) / direction.y;
				float tz1 = (chonk.position.z - position.z) / direction.z;
				float tz2 = (chonk.position.z + Chunk.CHUNK_LENGTH_Z - position.z) / direction.z;
				
				tMin = Math.max(tMin, Math.min(tx1, tx2));
				tMax = Math.min(tMax, Math.max(tx1, tx2));
				tMin = Math.max(tMin, Math.min(ty1, ty2));
				tMax = Math.min(tMax, Math.max(ty1, ty2));
				tMin = Math.max(tMin, Math.min(tz1, tz2));
				tMax = Math.min(tMax, Math.max(tz1, tz2));
				
				if(tMin < tMax && tMin > 0 && tMin < t) {
					chunk = chonk;
					t = tMin;
				}
			}
			
			if(chunk == null) {
				return null;
			}
			
			start.x = position.x + direction.x * (t + 0.0001f);
			start.y = position.y + direction.y * (t + 0.0001f);
			start.z = position.z + direction.z * (t + 0.0001f);
		}
		
		int xBlockCoord = (int) ((start.x - chunk.position.x) / Chunk.CELL_SIZE_X);
		int yBlockCoord = (int) ((start.y - chunk.position.y) / Chunk.CELL_SIZE_Y);
		int zBlockCoord = (int) ((start.z - chunk.position.z) / Chunk.CELL_SIZE_Z);
		
		//System.out.println("Block: [" + xBlockCoord + ", " + yBlockCoord + ", " + zBlockCoord + "]");
		
		int blockType = chunk.getCellValue(xBlockCoord, yBlockCoord, zBlockCoord);
		
		float error = 0.00001f;
		
		if(blockType != 0) {
			float tMin = Float.NEGATIVE_INFINITY;
			
			float tx1 = (chunk.position.x + xBlockCoord * Chunk.CELL_SIZE_X - position.x) / direction.x;
			float tx2 = (chunk.position.x + xBlockCoord * Chunk.CELL_SIZE_X + Chunk.CELL_SIZE_X - position.x) / direction.x;
			float ty1 = (chunk.position.y + yBlockCoord * Chunk.CELL_SIZE_Y - position.y) / direction.y;
			float ty2 = (chunk.position.y + yBlockCoord * Chunk.CELL_SIZE_Y + Chunk.CELL_SIZE_Y - position.y) / direction.y;
			float tz1 = (chunk.position.z + zBlockCoord * Chunk.CELL_SIZE_Z - position.z) / direction.z;
			float tz2 = (chunk.position.z + zBlockCoord * Chunk.CELL_SIZE_Z + Chunk.CELL_SIZE_Z - position.z) / direction.z;
			
			tMin = Math.max(tMin, Math.min(tx1, tx2));
			tMin = Math.max(tMin, Math.min(ty1, ty2));
			tMin = Math.max(tMin, Math.min(tz1, tz2));
			
			start.x = position.x + direction.x * tMin - chunk.position.x - xBlockCoord * Chunk.CELL_SIZE_X;
			start.y = position.y + direction.y * tMin - chunk.position.y - yBlockCoord * Chunk.CELL_SIZE_Y;
			start.z = position.z + direction.z * tMin - chunk.position.z - zBlockCoord * Chunk.CELL_SIZE_Z;
			
			if(start.x > -error && start.x < error && direction.x > 0) {
				blockDirection.set(-1, 0, 0);
			}else if(start.x > Chunk.CELL_SIZE_X - error && start.x < Chunk.CELL_SIZE_X + error && direction.x < 0) {
				blockDirection.set(1, 0, 0);
			}else if(start.y > -error && start.y < error && direction.y > 0) {
				blockDirection.set(0, -1, 0);
			}else if(start.y > Chunk.CELL_SIZE_Y - error && start.y < Chunk.CELL_SIZE_Y + error && direction.y < 0) {
				blockDirection.set(0, 1, 0);
			}else if(start.z > -error && start.z < error && direction.z > 0) {
				blockDirection.set(0, 0, -1);
			}else if(start.z > Chunk.CELL_SIZE_Z - error && start.z < Chunk.CELL_SIZE_Z + error && direction.z < 0){
				blockDirection.set(0, 0, 1);
			}else {
				//This should never happen, but just in case I messed up and it does...
				System.out.println("Failed to find voxel collision!");
			}
		}
		
		positionCount = 0;
		
		while(blockType == 0) {
			//Adds position to array so that I can display the blocks that have been intersected as a wire-frame
			if(positionCount >= blockPositions.size()) blockPositions.add(new Vector3f(0, 0, 0));
			Vector3f bPos = blockPositions.get(positionCount);
			bPos.x = chunk.position.x + xBlockCoord * Chunk.CELL_SIZE_X + Chunk.CELL_SIZE_X / 2.0f;
			bPos.y = chunk.position.y + yBlockCoord * Chunk.CELL_SIZE_Y + Chunk.CELL_SIZE_Y / 2.0f;
			bPos.z = chunk.position.z + zBlockCoord * Chunk.CELL_SIZE_Z + Chunk.CELL_SIZE_Z / 2.0f;
			positionCount++;
			
			//float tMin = Float.NEGATIVE_INFINITY;
			float tMax = Float.POSITIVE_INFINITY;
			
			float tx1 = (chunk.position.x + xBlockCoord * Chunk.CELL_SIZE_X - position.x) / direction.x;
			float tx2 = (chunk.position.x + xBlockCoord * Chunk.CELL_SIZE_X + Chunk.CELL_SIZE_X - position.x) / direction.x;
			float ty1 = (chunk.position.y + yBlockCoord * Chunk.CELL_SIZE_Y - position.y) / direction.y;
			float ty2 = (chunk.position.y + yBlockCoord * Chunk.CELL_SIZE_Y + Chunk.CELL_SIZE_Y - position.y) / direction.y;
			float tz1 = (chunk.position.z + zBlockCoord * Chunk.CELL_SIZE_Z - position.z) / direction.z;
			float tz2 = (chunk.position.z + zBlockCoord * Chunk.CELL_SIZE_Z + Chunk.CELL_SIZE_Z - position.z) / direction.z;
			
			//tMin = Math.max(tMin, Math.min(tx1, tx2));
			tMax = Math.min(tMax, Math.max(tx1, tx2));
			//tMin = Math.max(tMin, Math.min(ty1, ty2));
			tMax = Math.min(tMax, Math.max(ty1, ty2));
			//tMin = Math.max(tMin, Math.min(tz1, tz2));
			tMax = Math.min(tMax, Math.max(tz1, tz2));
			
			start.x = position.x + direction.x * tMax - chunk.position.x - xBlockCoord * Chunk.CELL_SIZE_X;
			start.y = position.y + direction.y * tMax - chunk.position.y - yBlockCoord * Chunk.CELL_SIZE_Y;
			start.z = position.z + direction.z * tMax - chunk.position.z - zBlockCoord * Chunk.CELL_SIZE_Z;
			
			//System.out.println("Start: [" + start.x + ", " + start.y + ", " + start.z + "]");
			
			if(start.x > -error && start.x < error && direction.x < 0) {
				//System.out.println("X--");
				blockDirection.set(1, 0, 0);
				xBlockCoord--;
				if(xBlockCoord < 0) {
					xBlockCoord = Chunk.GRID_SIZE_X - 1;
					chunk = chunk.leftChunk;
				}
			}else if(start.x > Chunk.CELL_SIZE_X - error && start.x < Chunk.CELL_SIZE_X + error && direction.x > 0) {
				//System.out.println("X++");
				blockDirection.set(-1, 0, 0);
				xBlockCoord++;
				if(xBlockCoord > Chunk.GRID_SIZE_X - 1) {
					xBlockCoord = 0;
					chunk = chunk.rightChunk;
				}
			}else if(start.y > -error && start.y < error && direction.y < 0) {
				//System.out.println("Y--");
				blockDirection.set(0, 1, 0);
				yBlockCoord--;
				if(yBlockCoord < 0) {
					yBlockCoord = Chunk.GRID_SIZE_Y - 1;
					chunk = chunk.bottomChunk;
				}
			}else if(start.y > Chunk.CELL_SIZE_Y - error && start.y < Chunk.CELL_SIZE_Y + error && direction.y > 0) {
				//System.out.println("Y++");
				blockDirection.set(0, -1, 0);
				yBlockCoord++;
				if(yBlockCoord > Chunk.GRID_SIZE_Y - 1) {
					yBlockCoord = 0;
					chunk = chunk.topChunk;
				}
			}else if(start.z > -error && start.z < error && direction.z < 0) {
				//System.out.println("Z--");
				blockDirection.set(0, 0, 1);
				zBlockCoord--;
				if(zBlockCoord < 0) {
					zBlockCoord = Chunk.GRID_SIZE_Z - 1;
					chunk = chunk.backChunk;
				}
			}else if(start.z > Chunk.CELL_SIZE_Z - error && start.z < Chunk.CELL_SIZE_Z + error && direction.z > 0){
				//System.out.println("Z++");
				blockDirection.set(0, 0, -1);
				zBlockCoord++;
				if(zBlockCoord > Chunk.GRID_SIZE_Z - 1) {
					zBlockCoord = 0;
					chunk = chunk.frontChunk;
				}
			}else {
				//This should never happen, but just in case I messed up and it does...
				System.out.println("Failed to find voxel collision!");
				break;
			}
			
			if(chunk == null) break;
			
			blockType = chunk.getCellValue(xBlockCoord, yBlockCoord, zBlockCoord);
		}
		
		xBlock = xBlockCoord;
		yBlock = yBlockCoord;
		zBlock = zBlockCoord;
		
		return chunk;
	}
	
	public void placeBlock(int x, int y, int z) {
		Entity cube1 = new Entity(model, new Vector3f(x * Chunk.CELL_SIZE_X + Chunk.CELL_SIZE_X / 2.0f, y * Chunk.CELL_SIZE_Y + Chunk.CELL_SIZE_Y / 2.0f, z * Chunk.CELL_SIZE_Z + Chunk.CELL_SIZE_Z / 2.0f), new Vector3f(0, 0, 0), new Vector3f(Chunk.CELL_SIZE_X / 2.0f, Chunk.CELL_SIZE_Y / 2.0f, Chunk.CELL_SIZE_Z / 2.0f));
		cube1.setColor(0.9f, 0.9f, 0.9f);
		entities.put(nextID, cube1);
		setBlockValue(x, y, z, nextID);
		nextID++;
	}
	
	public void removeBlock(int x, int y, int z) {
		int blockID = getBlockValue(x, y, z);
		entities.remove(blockID);
		setBlockValue(x, y, z, 0);
	}
	
	public void setBlockValue(int x, int y, int z, int value) {
		System.out.println("Placing block at: [" + x + ", " + y + ", " + z + "]");
		
		int xChunkCoord = x / Chunk.GRID_SIZE_X;
		int yChunkCoord = y / Chunk.GRID_SIZE_Y;
		int zChunkCoord = z / Chunk.GRID_SIZE_Z;
		
		if(x < 0 && x % 16 != 0) xChunkCoord--;
		if(y < 0 && y % 16 != 0) yChunkCoord--;
		if(z < 0 && z % 16 != 0) zChunkCoord--;
		
		System.out.println("Chunk coord: [" + xChunkCoord + ", " + yChunkCoord + ", " + zChunkCoord + "]");
		
		Chunk chunk = addChunkAtCoord(xChunkCoord, yChunkCoord, zChunkCoord);
		
		int xBlockCoord = x % Chunk.GRID_SIZE_X;
		int yBlockCoord = y % Chunk.GRID_SIZE_Y;
		int zBlockCoord = z % Chunk.GRID_SIZE_Z;
		
		//System.out.println("Block coord: [" + xBlockCoord + ", " + yBlockCoord + ", " + zBlockCoord + "]");
		
		if(xBlockCoord < 0) xBlockCoord += Chunk.GRID_SIZE_X;
		if(yBlockCoord < 0) yBlockCoord += Chunk.GRID_SIZE_Y;
		if(zBlockCoord < 0) zBlockCoord += Chunk.GRID_SIZE_Z;
		
		System.out.println("Block coord: [" + xBlockCoord + ", " + yBlockCoord + ", " + zBlockCoord + "]");
		
		chunk.setCellValue(xBlockCoord, yBlockCoord, zBlockCoord, value);
	}
	
	public int getBlockValue(int x, int y, int z) {
		System.out.println("Getting block at: [" + x + ", " + y + ", " + z + "]");
		
		int xChunkCoord = x / Chunk.GRID_SIZE_X;
		int yChunkCoord = y / Chunk.GRID_SIZE_Y;
		int zChunkCoord = z / Chunk.GRID_SIZE_Z;
		
		if(x < 0) xChunkCoord--;
		if(y < 0) yChunkCoord--;
		if(z < 0) zChunkCoord--;
		
		System.out.println("Chunk coord: [" + xChunkCoord + ", " + yChunkCoord + ", " + zChunkCoord + "]");
		
		Chunk chunk = addChunkAtCoord(xChunkCoord, yChunkCoord, zChunkCoord);
		
		int xBlockCoord = x % Chunk.GRID_SIZE_X;
		int yBlockCoord = y % Chunk.GRID_SIZE_Y;
		int zBlockCoord = z % Chunk.GRID_SIZE_Z;
		
		if(x < 0) xBlockCoord += Chunk.GRID_SIZE_X;
		if(y < 0) yBlockCoord += Chunk.GRID_SIZE_Y;
		if(z < 0) zBlockCoord += Chunk.GRID_SIZE_Z;
		
		System.out.println("Block coord: [" + xBlockCoord + ", " + yBlockCoord + ", " + zBlockCoord + "]");
		
		return chunk.getCellValue(xBlockCoord, yBlockCoord, zBlockCoord);
	}
	
	public Chunk addChunkAtCoord(int x, int y, int z) {
		Chunk chunk = getChunkAtCoord(x, y, z);
		if(chunk != null) {
			System.out.println("Chunk already exists at coordinate: [" + x + ", " + y + ", " + z + "]");
			return chunk;
		}
		
		chunk = new Chunk(x, y, z);
		
		chunk.leftChunk = getChunkAtCoord(x - 1, y, z);
		if(chunk.leftChunk != null) chunk.leftChunk.rightChunk = chunk;
		
		chunk.rightChunk = getChunkAtCoord(x + 1, y, z);
		if(chunk.rightChunk != null) chunk.rightChunk.leftChunk = chunk;
		
		chunk.topChunk = getChunkAtCoord(x, y + 1, z);
		if(chunk.topChunk != null) chunk.topChunk.bottomChunk = chunk;
		
		chunk.bottomChunk = getChunkAtCoord(x, y - 1, z);
		if(chunk.bottomChunk != null) chunk.bottomChunk.topChunk = chunk;
		
		chunk.frontChunk = getChunkAtCoord(x, y, z + 1);
		if(chunk.frontChunk != null) chunk.frontChunk.backChunk = chunk;
		
		chunk.backChunk = getChunkAtCoord(x, y, z - 1);
		if(chunk.backChunk != null) chunk.backChunk.frontChunk = chunk;
		
		return chunk;
	}
	
	public Chunk getChunkAtCoord(int xCoord, int yCoord, int zCoord) {
		//System.out.println("Getting chunk at coordinate: [" + xCoord + ", " + yCoord + ", " + zCoord + "]");
		ArrayList <Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(center);
		//int counter = 0;
		while(chunks.size() > 0) {
			//counter++;
			Chunk chunk = chunks.get(0);
			if(chunk.x == xCoord && chunk.y == yCoord && chunk.z == zCoord) {
				chunks.clear();
				chunks.add(center);
				while(chunks.size() > 0) {
					Chunk chunk2 = chunks.get(0);
					chunk2.visited = false;
					if(chunk2.topChunk != null && chunk2.topChunk.visited) chunks.add(chunk2.topChunk);
					if(chunk2.bottomChunk != null && chunk2.bottomChunk.visited) chunks.add(chunk2.bottomChunk);
					if(chunk2.rightChunk != null && chunk2.rightChunk.visited) chunks.add(chunk2.rightChunk);
					if(chunk2.leftChunk != null && chunk2.leftChunk.visited) chunks.add(chunk2.leftChunk);
					if(chunk2.frontChunk != null && chunk2.frontChunk.visited) chunks.add(chunk2.frontChunk);
					if(chunk2.backChunk != null && chunk2.backChunk.visited) chunks.add(chunk2.backChunk);
					chunks.remove(0);
				}
				//System.out.println("Returning discovered chunk, visited " + counter + " chunks");
				return chunk;
			}
			chunk.visited = true;
			if(chunk.topChunk != null && !chunk.topChunk.visited) chunks.add(chunk.topChunk);
			if(chunk.bottomChunk != null && !chunk.bottomChunk.visited) chunks.add(chunk.bottomChunk);
			if(chunk.rightChunk != null && !chunk.rightChunk.visited) chunks.add(chunk.rightChunk);
			if(chunk.leftChunk != null && !chunk.leftChunk.visited) chunks.add(chunk.leftChunk);
			if(chunk.frontChunk != null && !chunk.frontChunk.visited) chunks.add(chunk.frontChunk);
			if(chunk.backChunk != null && !chunk.backChunk.visited) chunks.add(chunk.backChunk);
			chunks.remove(0);
		}
		
		chunks.clear();
		chunks.add(center);
		while(chunks.size() > 0) {
			Chunk chunk2 = chunks.get(0);
			chunk2.visited = false;
			if(chunk2.topChunk != null && chunk2.topChunk.visited) chunks.add(chunk2.topChunk);
			if(chunk2.bottomChunk != null && chunk2.bottomChunk.visited) chunks.add(chunk2.bottomChunk);
			if(chunk2.rightChunk != null && chunk2.rightChunk.visited) chunks.add(chunk2.rightChunk);
			if(chunk2.leftChunk != null && chunk2.leftChunk.visited) chunks.add(chunk2.leftChunk);
			if(chunk2.frontChunk != null && chunk2.frontChunk.visited) chunks.add(chunk2.frontChunk);
			if(chunk2.backChunk != null && chunk2.backChunk.visited) chunks.add(chunk2.backChunk);
			chunks.remove(0);
		}
		//System.out.println("No chunk found at location");
		return null;
	}
	
	public ArrayList<Chunk> getAllChunks() {
		//System.out.println("Getting all chunks...");
		ArrayList <Chunk> chunks = new ArrayList<Chunk>();
		ArrayList <Chunk> temp = new ArrayList<Chunk>();
		temp.add(center);
		while(temp.size() > 0) {
			Chunk chunk = temp.get(0);
			chunks.add(chunk);
			chunk.visited = true;
			if(chunk.topChunk != null && !chunk.topChunk.visited) temp.add(chunk.topChunk);
			if(chunk.bottomChunk != null && !chunk.bottomChunk.visited) temp.add(chunk.bottomChunk);
			if(chunk.rightChunk != null && !chunk.rightChunk.visited) temp.add(chunk.rightChunk);
			if(chunk.leftChunk != null && !chunk.leftChunk.visited) temp.add(chunk.leftChunk);
			if(chunk.frontChunk != null && !chunk.frontChunk.visited) temp.add(chunk.frontChunk);
			if(chunk.backChunk != null && !chunk.backChunk.visited) temp.add(chunk.backChunk);
			temp.remove(0);
		}
		temp.add(center);
		while(temp.size() > 0) {
			Chunk chunk = temp.get(0);
			chunk.visited = false;
			if(chunk.topChunk != null && chunk.topChunk.visited) temp.add(chunk.topChunk);
			if(chunk.bottomChunk != null && chunk.bottomChunk.visited) temp.add(chunk.bottomChunk);
			if(chunk.rightChunk != null && chunk.rightChunk.visited) temp.add(chunk.rightChunk);
			if(chunk.leftChunk != null && chunk.leftChunk.visited) temp.add(chunk.leftChunk);
			if(chunk.frontChunk != null && chunk.frontChunk.visited) temp.add(chunk.frontChunk);
			if(chunk.backChunk != null && chunk.backChunk.visited) temp.add(chunk.backChunk);
			temp.remove(0);
		}
		//System.out.println("Returning discovered chunks");
		return chunks;
	}

}
