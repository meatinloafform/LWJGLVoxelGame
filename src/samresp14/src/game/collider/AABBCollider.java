package samresp14.src.game.collider;

import org.joml.Vector3f;
import org.joml.Vector3i;

import samresp14.src.game.World;
import samresp14.src.game.blocks.BlockState;
import samresp14.src.game.blocks.Blocks;

public class AABBCollider extends Collider {
	public Vector3f position;
	public Vector3f size;
	
	public AABBCollider(Vector3f position, Vector3f size) {
		this.position = position;
		this.size = size;
	}
	
	@Override
	public CollisionResult resolve(Collider other) {
		if (other instanceof AABBCollider) {
			AABBCollider otherAABB = (AABBCollider)other;
			
			if (intersect(otherAABB.position, otherAABB.size)) {
				System.out.println("Buh !!!!!");
			}
			
			return null;
		} else if (other instanceof WorldCollider) {
			WorldCollider collider = (WorldCollider)other;
			Vector3i tilePos = World.worldToBlockSpace(position);
			for (int i = 0; i < BLOCK_OFFSET_CHECKS.length; i += 3) {
				Vector3i checkPos = new Vector3i(tilePos.x + BLOCK_OFFSET_CHECKS[i], tilePos.y + BLOCK_OFFSET_CHECKS[i + 1], tilePos.z + BLOCK_OFFSET_CHECKS[i + 2]);
				if (collider.world.getBlock(checkPos) != BlockState.AIR) {
					collider.world.setBlock(checkPos, Blocks.BLOCK_AIR);
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	private boolean intersect(Vector3f otherPos, Vector3f otherSize) {
		Vector3f otherMax = otherPos.add(otherSize);
		Vector3f max = position.add(size);
		
		return (
			position.x <= otherMax.x &&
			max.x >= otherPos.x &&
			position.y <= otherMax.y &&
			max.y >= otherPos.y &&
			position.z <= otherMax.z &&
			max.z >= otherPos.z
		);
	}
	
	private static final int[] BLOCK_OFFSET_CHECKS = new int[] {
		0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0,
		0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0,
		0, 2, 0, 1, 2, 0, 0, 2, 1, 1, 2, 0
	};
}
