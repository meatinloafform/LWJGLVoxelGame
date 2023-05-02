package samresp14.src.game.blocks;

import samresp14.src.game.blocks.properties.DirectionProperty;
import samresp14.src.util.Direction;

public class FacingBlock extends Block {
	public int facing_property;
	
	public FacingBlock(Direction facing) {
		super();
		facing_property = withProperty(new DirectionProperty(facing));
	}
	
	public Direction facing(BlockState state) {
		return ((DirectionProperty)(state.properties.get(facing_property))).value;
	}
}
