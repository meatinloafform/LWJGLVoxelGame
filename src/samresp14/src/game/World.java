package samresp14.src.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;
import org.joml.Vector3i;

import samresp14.src.Renderer;
import samresp14.src.Texture;
import samresp14.src.game.blocks.Block;
import samresp14.src.game.blocks.BlockState;
import samresp14.src.game.worldgen.ProtoChunk;
import samresp14.src.game.worldgen.WorldBuilder;

public class World {
	private List<Chunk> chunks;
	private List<Integer> dirtyChunks;
	private Texture atlas;
	public WorldBuilder builder;
	private List<ProtoChunk> protoChunks;
	
	public World(Texture atlas) {
		chunks = new ArrayList<>();
		this.atlas = atlas;
		dirtyChunks = new ArrayList<>();
		builder = new WorldBuilder();
		protoChunks = new ArrayList<>();
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
	
	private Vector2i getChunkPos(Vector2i position) {
		return new Vector2i(position.x >= 0 ? position.x / 16 : (position.x / 16) - 1, position.y >= 0 ? position.y / 16 : (position.y / 16) - 1);
	}
	
	private int chunkMod(int pos) {
		return pos >= 0 ? pos % 16 : 16 - (Math.abs(pos) % 16);
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
		Vector2i chunkPos = new Vector2i(position.x, position.z).div(16);
		Chunk chunk = getChunk(chunkPos);
		if (chunk != null) {
			return chunk.getBlock(position.x % 16, position.y, position.z % 16);
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
				markNeighborsDirty(chunk);
				chunk.changed = false;
			}
		}
	}
	
	public void render(Renderer renderer) {
		for (Chunk chunk : chunks) {
			if (chunk.mesh != null) {
				renderer.draw(chunk.mesh);
			}
		}
	}
}
