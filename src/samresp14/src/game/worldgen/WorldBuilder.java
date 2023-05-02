package samresp14.src.game.worldgen;

import java.util.Random;

public class WorldBuilder {
	private long seed;
	private Random random;
	
	public WorldBuilder() {
		random = new Random();
		seed = random.nextLong();
		random.setSeed(seed);
	}
	
	public WorldBuilder(long seed) {
		this.seed = seed;
		random = new Random(seed);
	}
}
