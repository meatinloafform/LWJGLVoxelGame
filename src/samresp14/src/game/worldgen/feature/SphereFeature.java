package samresp14.src.game.worldgen.feature;

import org.joml.Vector3i;

import samresp14.src.game.World;
import samresp14.src.game.blocks.Block;

public class SphereFeature extends WorldFeature {

	public Vector3i position;
	public Block block;
	public int radius;
	
	public SphereFeature(Vector3i position, Block block, int radius) {
		this.position = position;
		this.block = block;
		this.radius = radius;
	}
	
	@Override
	public void apply(World world) {
		for (int z = position.z - radius; z <= position.z + radius; z++) {
			for (int y = position.y - radius; y <= position.y + radius; y++) {
				for (int x = position.x - radius; x <= position.x + radius; x++) {
					int dX = position.x - x;
					int dY = position.y - y;
					int dZ = position.z - z;
					if (dX*dX+dY*dY+dZ*dZ < radius*radius) {
						world.setBlock(new Vector3i(x, y, z), block);
					}
				}
			}
		}
	}
	
}
