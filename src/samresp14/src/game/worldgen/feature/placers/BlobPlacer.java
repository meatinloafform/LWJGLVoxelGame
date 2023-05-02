package samresp14.src.game.worldgen.feature.placers;

import org.joml.Vector3i;

import samresp14.src.game.World;
import samresp14.src.game.blocks.Block;

public class BlobPlacer extends Placer {
	
	public Vector3i position;
	public Block block;
	public Vector3i extents;
	
	public BlobPlacer(Vector3i position, Block block, Vector3i extents) {
		this.position = position;
		this.block = block;
		this.extents = extents;
	}
	
	@Override
	public void place(World world) {
		for (int z = -extents.z + position.z; z <= extents.z + position.z; z++) {
			for (int y = -extents.y + position.y; y <= extents.y + position.y; y++) {
				for (int x = -extents.x + position.x; x <= extents.x + position.x; x++) {
					// (x^2/a^2)+(y^2/b^2)+(z^2/c^2) < 1
					float dX = position.x - x;
					float dY = position.y - y;
					float dZ = position.z - z;
					//System.out.println(((dX*dX)/(extents.x*extents.x))+((dY*dY)/(extents.y*extents.y))+((dZ*dZ)/(extents.z*extents.z)));
					if (((dX*dX)/(extents.x*extents.x))+((dY*dY)/(extents.y*extents.y))+((dZ*dZ)/(extents.z*extents.z)) < 1f) {
						world.setBlock(new Vector3i(x, y, z), block);
					}
				}
			}
		}
	}
}
