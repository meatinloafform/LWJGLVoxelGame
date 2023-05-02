package samresp14.src.game.worldgen;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;
import org.joml.Vector3i;

import samresp14.src.game.Chunk;
import samresp14.src.game.World;
import samresp14.src.game.blocks.Block;

public class ProtoChunk {
	public List<QueuedPlacement> placements;
	public Vector2i position;
	
	public ProtoChunk(Vector2i position) {
		placements = new ArrayList<>();
		this.position = position;
	}
	
	public void queuePlacement(Vector3i pos, Block block) {
		queuePlacement(pos, block, 0);
	}
	
	public void queuePlacement(Vector3i pos, Block block, int priority) {
		placements.add(new QueuedPlacement(pos, block, priority));
	}
	
	public Chunk build(World world, WorldBuilder builder) {
		world.addChunk(position);
		Chunk chunk = world.addChunk(position);
		for (QueuedPlacement placement : placements) {
			chunk.setBlock(placement.block.getDefaultBlockState(), placement.pos.x, placement.pos.y, placement.pos.z);
		}
		return chunk;
	}
	
	class QueuedPlacement {
		Vector3i pos;
		Block block;
		int priority;
		
		QueuedPlacement(Vector3i pos, Block block) {
			this.pos = pos;
			this.block = block;
			this.priority = 0;
		}
		
		QueuedPlacement(Vector3i pos, Block block, int priority) {
			this.pos = pos;
			this.block = block;
			this.priority = priority;
		}
	}
}
