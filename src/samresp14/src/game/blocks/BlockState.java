package samresp14.src.game.blocks;

import java.util.ArrayList;
import java.util.List;

import samresp14.src.game.blocks.properties.Property;

public class BlockState {
	public short id;
	public List<Property> properties;
	
	public BlockState(short id) {
		this.id = id;
		this.properties = new ArrayList<>();
	}
	
	public static final BlockState AIR = new BlockState((short)0);
}
 