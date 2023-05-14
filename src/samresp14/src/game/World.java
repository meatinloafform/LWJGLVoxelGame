package samresp14.src.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import samresp14.src.Camera;
import samresp14.src.Texture;
import samresp14.src.game.blocks.Block;
import samresp14.src.game.blocks.BlockState;
import samresp14.src.game.player.Player;
import samresp14.src.game.worldgen.ProtoChunk;
import samresp14.src.game.worldgen.WorldBuilder;
import samresp14.src.rendering.MeshRenderer;

public class World {
	private List<Chunk> chunks;
	private List<Integer> dirtyChunks;
	private Texture atlas;
	public WorldBuilder builder;
	private List<ProtoChunk> protoChunks;
	public Player player;
	
	public World(Texture atlas, Camera camera) {
		chunks = new ArrayList<>();
		this.atlas = atlas;
		dirtyChunks = new ArrayList<>();
		builder = new WorldBuilder();
		protoChunks = new ArrayList<>();
		player = new Player(camera, new Vector3f());
	}
	
	private ProtoChunk addProtoChunk(Vector2i position) {
		protoChunks.add(new ProtoChunk(position));
		return protoChunks.get(protoChunks.size() - 1);
	}
	
	private ProtoChunk getProtoChunk(Vector2i position) {
		for (ProtoChunk proto : protoChunks) {
			if (proto.position.equals(position)) {
				return proto;
			}
		}
		
		return null;
	}
	
	public static Vector3i worldToBlockSpace(Vector3f world) {
		return new Vector3i(
			(int)(world.x + (world.x > 0 ? 0.5 : -0.5)),
			(int)(world.y + (world.y > 0 ? 0.5 : -0.5)),
			(int)(world.z + (world.z > 0 ? 0.5 : -0.5))
		);
	}
	
	public static Vector3f blockToWorldSpace(Vector3i block) {
		return new Vector3f(
			((float)block.x) - 0.5f,
			((float)block.y) - 0.5f,
			((float)block.z) - 0.5f
		);
	}
	
	public void markNeighborsDirty(Chunk chunk) {
		if (chunk.neighborEast != null && !dirtyChunks.contains(chunk.neighborEast.getID())) {
			dirtyChunks.add(chunk.neighborEast.getID());
		}
		if (chunk.neighborWest != null && !dirtyChunks.contains(chunk.neighborWest.getID())) {
			dirtyChunks.add(chunk.neighborWest.getID());
		}
		if (chunk.neighborSouth != null && !dirtyChunks.contains(chunk.neighborSouth.getID())) {
			dirtyChunks.add(chunk.neighborSouth.getID());
		}
		if (chunk.neighborNorth != null && !dirtyChunks.contains(chunk.neighborNorth.getID())) {
			dirtyChunks.add(chunk.neighborNorth.getID());
		}
	}
	
	public Chunk addChunk(Vector2i position) {
		Chunk chunk = null;
		for (int i = 0; i < chunks.size(); i++) {
			if (chunks.get(i).position.equals(position)) {
				chunk = new Chunk(i, position);
				chunk.copyNeighbors(chunks.get(i));
				chunks.set(i, chunk);
				dirtyChunks.add(i);
			}
		}
		
		if (chunk == null) {
			chunk = new Chunk(chunks.size(), position);
			for (int i = 0; i < chunks.size(); i++) {
				Vector2i diff = new Vector2i(position).sub(chunks.get(i).position);
				if (diff.x == 0 && diff.y == 1) {
					// North
					chunks.get(i).neighborNorth(chunk);
					chunk.neighborSouth(chunks.get(i));
				} else if (diff.x == 0 && diff.y == -1) {
					// South
					chunks.get(i).neighborSouth(chunk);
					chunk.neighborNorth(chunks.get(i));
				} else if (diff.x == -1 && diff.y == 0) {
					// West
					chunks.get(i).neighborWest(chunk);
					chunk.neighborEast(chunks.get(i));
				} else if (diff.x == 1 && diff.y == 0) {
					// East
					chunks.get(i).neighborEast(chunk);
					chunk.neighborWest(chunks.get(i));
				}
			}
			
			dirtyChunks.add(chunks.size());
			chunks.add(chunk);
		}
		
		return chunk;
	}
	
	public Vector2i getChunkPos(Vector2i position) {
		return new Vector2i(position.x >= 0 ? position.x / 16 : ((position.x + 1) / 16) - 1, position.y >= 0 ? position.y / 16 : ((position.y + 1) / 16) - 1);
	}
	
	private int chunkMod(int pos) {
		//System.out.println(16 - (Math.abs(pos) % 16));
		return pos >= 0 ? pos % 16 : 15 - (Math.abs(pos + 1) % 16);
	}
	
	private Vector3i chunkPlacementPos(Vector3i position) {
		return new Vector3i(chunkMod(position.x), position.y, chunkMod(position.z));
	}
	
	public void setBlock(Vector3i position, Block block) {
		if (position.y < 0 || position.y >= 64) { return; }
		Vector2i chunkPos = getChunkPos(new Vector2i(position.x, position.z));
		Chunk chunk = getChunk(chunkPos);
		
		if (chunk == null) {
			ProtoChunk proto = getProtoChunk(chunkPos);
			
			if (proto != null) {
				proto.queuePlacement(chunkPlacementPos(position), block);
			} else {
				proto = addProtoChunk(chunkPos);
				proto.queuePlacement(chunkPlacementPos(position), block);
			}
		}
		
		if (chunk != null) {
			chunk.setBlock(block.getDefaultBlockState(), chunkMod(position.x), position.y, chunkMod(position.z));
			if (!dirtyChunks.contains(chunk.getID())) {
				dirtyChunks.add(chunk.getID());
			}
		}
	}
	
	public BlockState getBlock(Vector3i position) {
		if (position.y < 0 || position.y >= 64) { return BlockState.AIR; }
		
		Vector2i chunkPos = getChunkPos(new Vector2i(position.x, position.z));
		Chunk chunk = getChunk(chunkPos);
		if (chunk != null) {
			return chunk.getBlock(chunkMod(position.x), position.y, chunkMod(position.z));
		}
		
		return BlockState.AIR;
	}
	
	public Chunk getChunk(Vector2i position) {		
		for (Chunk chunk : chunks) {
			if (chunk.position.equals(position)) {
				return chunk;
			}
		}
		return null;
	}
	
	public void update() {
		for (int dirty : dirtyChunks) {
			chunks.get(dirty).generateMesh(atlas);
		}
		dirtyChunks.clear();
		
		for (ProtoChunk proto : protoChunks) {
			Chunk chunk = proto.build(this, builder);
			dirtyChunks.add(chunk.getID());
		}
		protoChunks.clear();
		
		for (Chunk chunk : chunks) {
			if (chunk.changed) {
				if (!dirtyChunks.contains(chunk.getID())) {
					dirtyChunks.add(chunk.getID());
				}
				markNeighborsDirty(chunk);
				chunk.changed = false;
			}
		}
	}
	
	public void render(MeshRenderer renderer) {
		for (Chunk chunk : chunks) {
			if (chunk.mesh != null) {
				renderer.draw(chunk.mesh);
			}
		}
	}
}
