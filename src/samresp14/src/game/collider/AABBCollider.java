package samresp14.src.game.collider;

import org.joml.Vector3f;

public class AABBCollider extends Collider {
	public Vector3f position;
	public Vector3f size;
	
	@Override
	public CollisionResult resolve(Collider other) {
		if (other instanceof AABBCollider) {
			AABBCollider otherAABB = (AABBCollider)other;
			
			if (intersect(otherAABB.position, otherAABB.size)) {
				System.out.println("Buh !!!!!");
			}
			
			return null;
		} else if (other instanceof WorldCollider) {
			
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
}
