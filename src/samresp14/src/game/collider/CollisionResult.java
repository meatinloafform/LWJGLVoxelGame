package samresp14.src.game.collider;

import org.joml.Vector3f;

public class CollisionResult {
	public boolean collision;
	public Vector3f resolution;
	
	public CollisionResult() {
		this.collision = false;
		this.resolution = null;
	}
	
	public CollisionResult(Vector3f resolution) {
		this.collision = true;
		this.resolution = resolution;
	}
}
