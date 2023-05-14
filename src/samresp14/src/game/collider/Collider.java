package samresp14.src.game.collider;

import org.joml.Vector3f;

public abstract class Collider {
	//public abstract CollisionResult resolve(Collider other);
	public abstract CollisionResult move(Vector3f velocity);
}
