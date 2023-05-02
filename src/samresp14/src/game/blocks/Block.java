package samresp14.src.game.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2i;

import samresp14.src.game.blocks.properties.Property;
import samresp14.src.util.Direction;

public class Block {
	public int id;
	public Map<Direction, Vector2i> texSides;
	private List<Property> defaultProperties;
	
	public Block() {
		defaultProperties = new ArrayList<>();
		texSides = new HashMap<>();
		texSides.put(Direction.UP, new Vector2i());
		texSides.put(Direction.DOWN, new Vector2i());
		texSides.put(Direction.LEFT, new Vector2i());
		texSides.put(Direction.RIGHT, new Vector2i());
		texSides.put(Direction.FRONT, new Vector2i());
		texSides.put(Direction.BACK, new Vector2i());
	}
	
	public Block texAll(Vector2i texPos) {
		texSides.put(Direction.UP, texPos);
		texSides.put(Direction.DOWN, texPos);
		texSides.put(Direction.LEFT, texPos);
		texSides.put(Direction.RIGHT, texPos);
		texSides.put(Direction.FRONT, texPos);
		texSides.put(Direction.BACK, texPos);
		return this;
	}
	
	public Vector2i getSideTexture(Direction side) {
		return texSides.get(side);
	}
	
	public Block texSide(Direction side, Vector2i texPos) {
		texSides.put(side, texPos);
		return this;
	}
	
	public BlockState getDefaultBlockState() {
		BlockState state = new BlockState((short)this.id);
		for (Property prop : defaultProperties) {
			//state.properties.add((Property)prop.clone());
		}
		return state;
	}
	
	public int withProperty(Property property) {
		defaultProperties.add(property);
		return defaultProperties.size() - 1;
	}
}
