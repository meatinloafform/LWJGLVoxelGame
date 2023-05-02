package samresp14.src.game.collider;

import samresp14.src.game.World;

public abstract class Collider {
	public abstract CollisionResult resolve(Collider other);
}
