package samresp14.src.game.collider;

import samresp14.src.game.World;

public class WorldCollider extends Collider {
	public World world;
	
	public WorldCollider(World world) {
		this.world = world;
	}
	
	@Override
	public CollisionResult resolve(Collider other) {
		if (other instanceof AABBCollider) {
			return null;
		} else {
			return null;
		}
	}
	
//	private boolean intersect(AABBCollider other) {
//		
//	}
}
