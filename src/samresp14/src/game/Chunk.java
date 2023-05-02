package samresp14.src.game;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import samresp14.src.Texture;
import samresp14.src.game.blocks.BlockState;
import samresp14.src.game.blocks.Blocks;
import samresp14.src.mesh.Mesh;
import samresp14.src.mesh.MeshQuadBuilder;
import samresp14.src.util.Direction;

public class Chunk {
	public Mesh mesh;
	private BlockState[][][] blocks;
	private int id;
	public Vector2i position;
	public Chunk neighborNorth;
	public Chunk neighborEast;
	public Chunk neighborSouth;
	public Chunk neighborWest;
	private Matrix4f model;
	public boolean changed = false;
	
	public Chunk(int id, Vector2i position) {
		blocks = new BlockState[16][64][16];
		for (int z = 0; z < 16; z++) {
			for (int y = 0; y < 64; y++) {
				for (int x = 0; x < 16; x++) {
					blocks[z][y][x] = BlockState.AIR;
				}
			}
		}
		this.id = id;
		this.position = position;
		model = new Matrix4f().translate(new Vector3f(position.x * 16f, 0f, position.y * 16f));
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	private void addDirQuad(int i, int x, int y, int z, short blockID, MeshQuadBuilder builder) {
		Direction dir = NEIGHBOR_DIRECTIONS[i / 3];
		builder.addQuad(dir, new Vector3f(x, y, z), Blocks.getBlock(blockID).getSideTexture(dir));
	}
	
	public void generateMesh(Texture atlas) {
		MeshQuadBuilder builder = new MeshQuadBuilder(atlas);
		for (int z = 0; z < blocks.length; z++) {
			for (int y = 0; y < blocks[0].length; y++) {
				for (int x = 0; x < blocks[0][0].length; x++) {
					if (blocks[z][y][x].id == 0) { continue; }
					short blockID = blocks[z][y][x].id;
					for (int i = 0; i < NEIGHBORS.length; i += 3) {
						int pX = x + NEIGHBORS[i];
						int pY = y + NEIGHBORS[i + 1];
						int pZ = z + NEIGHBORS[i + 2];
						
						if (   pX >= 0 && pX < blocks[0][0].length 
							&& pY >= 0 && pY < blocks[0].length
							&& pZ >= 0 && pZ < blocks.length) {
							if (blocks[pZ][pY][pX].id == 0) {
								addDirQuad(i, x, y, z, blockID, builder);
							}
						} else {
							if (pX < 0 && neighborWest != null) {
								// West
								if (neighborWest.blocks[pZ][pY][15].id == 0) {
									addDirQuad(i, x, y, z, blockID, builder);
								}
							}
							
							if (pX >= blocks[0][0].length && neighborEast != null) {
								// East
								if (neighborEast.blocks[pZ][pY][0].id == 0) {
									addDirQuad(i, x, y, z, blockID, builder);
								}
							}
							
							if (pZ < 0 && neighborSouth != null) {
								// South
								if (neighborSouth.blocks[15][pY][pX].id == 0) {
									addDirQuad(i, x, y, z, blockID, builder);
								}
							}
							
							if (pZ >= blocks.length && neighborNorth != null) {
								// North
								if (neighborNorth.blocks[0][pY][pX].id == 0) {
									addDirQuad(i, x, y, z, blockID, builder);
								}
							}
							
							if (pY < 0 || pY >= blocks[0].length) {
								addDirQuad(i, x, y, z, blockID, builder);
							}
						}
					}
				}
			}
		}
		mesh = builder.build();
		mesh.setModel(model);
	}
	
	public BlockState getBlock(int x, int y, int z) {
		return blocks[z][y][x];
	}
	
	public void setBlock(BlockState b, int x, int y, int z) {
		blocks[z][y][x] = b;
		changed = true;
	}
	
	private static final int[] NEIGHBORS = {
		0, 0, 1,
		0, 0, -1,
		0, 1, 0,
		0, -1, 0,
		1, 0, 0,
		-1, 0, 0		
	};
	
	public void copyNeighbors(Chunk other) {
		other.neighborEast = neighborEast;
		other.neighborNorth = neighborNorth;
		other.neighborSouth = neighborSouth;
		other.neighborWest = neighborWest;
	}
	
	private static final Direction[] NEIGHBOR_DIRECTIONS = {
		Direction.FRONT, Direction.BACK, Direction.UP, Direction.DOWN, Direction.RIGHT, Direction.LEFT
	};
	
	public void neighborNorth(Chunk neighbor) {
		neighborNorth = neighbor;
	}
	
	public void neighborEast(Chunk neighbor) {
		neighborEast = neighbor;
	}
	
	public void neighborSouth(Chunk neighbor) {
		neighborSouth = neighbor;
	}
	
	public void neighborWest(Chunk neighbor) {
		neighborWest = neighbor;
	}
}
