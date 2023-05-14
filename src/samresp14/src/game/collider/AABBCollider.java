package samresp14.src.game.collider;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector3i;

import samresp14.src.Color;
import samresp14.src.game.World;
import samresp14.src.game.blocks.BlockState;
import samresp14.src.util.VectorUtil;

public class AABBCollider {
	public Vector3f position;
	public Vector3f size;
	
	public AABBCollider(Vector3f position, Vector3f size) {
		this.position = position;
		this.size = size;
	}
	
	public Vector3f collidePlanarWorld(World world) {
		Vector3i tilePos = World.worldToBlockSpace(position);
		
		// bottom, right, left, top, front, back
		float[] areas = new float[6];
		Vector3f[] offsets = new Vector3f[6];
		Vector3f maxOffset = new Vector3f();
		
		int sizeZ = (int)(Math.ceil(size.z)) + 1;
		int sizeY = (int)(Math.ceil(size.y)) + 1;
		int sizeX = (int)(Math.ceil(size.x)) + 1;
		
		AABBCollider block = new AABBCollider(new Vector3f(), new Vector3f(1f));
		for (int z = 0; z < sizeZ; z++) {
			for (int y = 0; y < sizeY; y++) {
				for (int x = 0; x < sizeX; x++) {
					Vector3i checkPos = new Vector3i(tilePos.x + x, tilePos.y + y, tilePos.z + z);
					if (world.getBlock(checkPos) != BlockState.AIR) {
						block.position = World.blockToWorldSpace(checkPos);
						
						// Bottom
						if (position.y < block.position.y + 1f && position.y > block.position.y) {
							if (rectIntersect(position.x, position.z, size.x, size.z, block.position.x, block.position.z, 1f, 1f)) {
								areas[0] += intersectionArea(position.x, position.z, size.x, size.x, block.position.x, block.position.z, 1f, 1f);
								offsets[0] = new Vector3f(0f, (block.position.y + 1f) - position.y, 0f);
							}
						}
						
						// Right
						if (position.x + size.x > block.position.x && position.x + size.x < block.position.x + 1f) {
							if (rectIntersect(position.z, position.y, size.z, size.y, block.position.z, block.position.y, 1f, 1f)) {
								areas[1] += intersectionArea(position.z, position.y, size.z, size.y, block.position.z, block.position.y, 1f, 1f);
								offsets[1] = new Vector3f(block.position.x - (position.x + size.x), 0f, 0f);
							}
						}
						
						// Left
						if (position.x < block.position.x + 1f && position.x >  block.position.x) {
							if (rectIntersect(position.z, position.y, size.z, size.y, block.position.z, block.position.y, 1f, 1f)) {
								areas[2] += intersectionArea(position.z, position.y, size.z, size.y, block.position.z, block.position.y, 1f, 1f);
								offsets[2] = new Vector3f((block.position.x + 1f) - position.x, 0f, 0f);
							}
						}
						
						// Top
						if (position.y + size.y > block.position.y && position.y + size.y < block.position.y + 1f) {
							if (rectIntersect(position.x, position.z, size.x, size.z, block.position.x, block.position.z, 1f, 1f)) {
								areas[3] += intersectionArea(position.x, position.z, size.x, size.x, block.position.x, block.position.z, 1f, 1f);
								offsets[3] = new Vector3f(0f, block.position.y - (position.y + size.y), 0f);
							}
						}
						
						// Front
						if (position.z + size.z > block.position.z && position.z + size.x < block.position.z + 1) {
							if (rectIntersect(position.x, position.y, size.x, size.y, block.position.x, block.position.y, 1f, 1f)) {
								areas[4] += intersectionArea(position.x, position.y, size.x, size.y, block.position.x, block.position.y, 1f, 1f);
								offsets[4] = new Vector3f(0f, 0f, block.position.z - (position.z + size.z));
							}
						}
						
						// Back
						if (position.z < block.position.z + 1f && position.z > block.position.z) {
							if (rectIntersect(position.x, position.y, size.x, size.y, block.position.x, block.position.y, 1f, 1f)) {
								areas[5] += intersectionArea(position.x, position.y, size.x, size.y, block.position.x, block.position.y, 1f, 1f);
								offsets[5] = new Vector3f(0f, 0f, (block.position.z + 1) - position.z);
							}
						}
					}
				}
			}
		}
		
		float maxArea = 0f;
		int maxIndex = -1;
		for (int i = 0; i < areas.length; i++) {
			if (areas[i] > maxArea) {
				maxArea = areas[i];
				maxIndex = i;
			}
		}
		
		if (maxArea == 0f) {
			return new Vector3f();
		}
		
		return offsets[maxIndex];
	}
	
	public static boolean rectIntersect(float aX, float aY, float aSzX, float aSzY, float bX, float bY, float bSzX, float bSzY) {
		return !(
				   bX > aX + aSzX
				|| bX + bSzX < aX
				|| bY > aY + aSzY
				|| bY + bSzY < aY
		);
	}
	
	public static float intersectionArea(float aX, float aY, float aSzX, float aSzY, float bX, float bY, float bSzX, float bSzY) {
		return Math.max(0, Math.min(aX + aSzX, bX + bSzX) - Math.max(aX, bX))
				* Math.max(0, Math.min(aY + aSzY, bY + bSzY) - Math.max(aY, bY));
	}
}
