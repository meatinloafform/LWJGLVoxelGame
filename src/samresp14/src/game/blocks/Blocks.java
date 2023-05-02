package samresp14.src.game.blocks;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import samresp14.src.util.Direction;

public class Blocks {
	private static List<Block> blocks = new ArrayList<>();
	
	public static final Block BLOCK_AIR = register(new Block());
	public static final Block BLOCK_BRICK = register(new Block().texAll(new Vector2i(1, 0)));
	public static final Block BLOCK_GHOST = register(new Block().texAll(new Vector2i(0, 1)));
	public static final Block BLOCK_WHITE = register(new Block()
			.texAll(new Vector2i(1, 2))
			.texSide(Direction.UP, new Vector2i(2, 1))
			.texSide(Direction.DOWN, new Vector2i(2, 1))
	);
	
	public static Block register(Block block) {
		block.id = blocks.size();
		blocks.add(block);
		return block;
	}
	
	public static Block getBlock(short block) {
		return blocks.get((int)block);
	}
}
