package samresp14.src.mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Vector2i;
import org.joml.Vector3f;

import samresp14.src.Texture;
import samresp14.src.util.Direction;

public class MeshQuadBuilder {
	public int points;
	private List<Float> vertices;
	private List<Integer> indices;
	private List<Float> uvs;
	private Texture texture;
	
	public MeshQuadBuilder(Texture texture) {
		points = 0;
		vertices = new ArrayList<>();
		indices = new ArrayList<>();
		uvs = new ArrayList<>();
		this.texture = texture;
	}
	
	public void addQuad(Direction side, Vector3f position, Vector2i atlasCoord) {
		float[] quad = translate(quadBySide(side), position);
		for (float v : quad) {
			vertices.add(v);
		}
		
		int[] indices = quadIndices();		
		for (int i : indices) {
			this.indices.add(i);
		}
		
//		float[] uvs = {
//				0.0f, 0.0f,
//				1.0f, 0.0f,
//				0.0f, 1.0f,
//				1.0f, 1.0f,
//		};
		
		float[] uvs = getUVS(atlasCoord, texture);
		
		for (float u : uvs) {
			this.uvs.add(u);
		}
		
		points += 4;
	}
	
	public Mesh build() {
		float[] vertices = new float[this.vertices.size()];
		int[] indices = new int[this.indices.size()];
		float[] uvs = new float[this.uvs.size()];
		for (int i = 0; i < this.vertices.size(); i++) {
			vertices[i] = this.vertices.get(i);
		}
		for (int i = 0; i < this.indices.size(); i++) {
			indices[i] = this.indices.get(i);
		}
		for (int i = 0; i < this.uvs.size(); i++) {
			uvs[i] = this.uvs.get(i);
		}
		return MeshLoader.createMesh(vertices, uvs, indices, texture);
	}
	
	private static float[] translate(float[] in, Vector3f translate) {
		float[] new_quad = new float[in.length];
		for (int i = 0; i < in.length; i++) {
			float v = in[i];
			if (i % 3 == 0) {
				new_quad[i] = v + translate.x;
			} else if (i % 3 == 1) {
				new_quad[i] = v + translate.y;
			} else {
				new_quad[i] = v + translate.z;
			}
		}
		return new_quad;
	}
	
	private static<T> T[] extend(T[] in, T[] extend) {
		T[] res = Arrays.copyOf(in, in.length + extend.length);
		System.arraycopy(extend, 0, res, in.length, extend.length);
		return res;
	}
	
	private int[] quadIndices() {
		return new int[] {
			0 + points, 1 + points, 2 + points, 1 + points, 3 + points, 2 + points
		};
	}
	
	private static float[] quadBySide(Direction side) {
		switch (side) {
		case BACK: return QUAD_BACK;
		case UP: return QUAD_TOP;
		case DOWN: return QUAD_BOTTOM;
		case LEFT: return QUAD_LEFT;
		case RIGHT: return QUAD_RIGHT;
		case FRONT: 
		default: return QUAD_FRONT;
		}
	}
	
	public static float[] getUVS(Vector2i atlasCoord, Texture texture) {
		float tileXUnit = 1f / (float)(texture.getWidth() / 16);
		float tileYUnit = 1f / (float)(texture.getHeight() / 16);
		return new float[] {
			atlasCoord.x * tileXUnit, 1f - (atlasCoord.y * tileYUnit),
			(atlasCoord.x + 1) * tileXUnit, 1f - (atlasCoord.y * tileYUnit),
			atlasCoord.x * tileXUnit, 1f - ((atlasCoord.y + 1) * tileYUnit),
			(atlasCoord.x + 1) * tileXUnit, 1f - ((atlasCoord.y + 1) * tileYUnit),
		};
	}
	
	private static float[] QUAD_TOP = {
	          // Top face
	         -0.5f, 0.5f, 0.5f,
	          0.5f, 0.5f, 0.5f,
	         -0.5f, 0.5f, -0.5f,
	          0.5f, 0.5f, -0.5f
	};
	
	private static float[] QUAD_BOTTOM = {
	          // Bottom face
			  0.5f, -0.5f, 0.5f,
	         -0.5f, -0.5f, 0.5f,
	          0.5f, -0.5f, -0.5f,
	         -0.5f, -0.5f, -0.5f
	};
	
	private static float[] QUAD_LEFT = {
	          // Left face
			  -0.5f, -0.5f, -0.5f,
	          -0.5f, -0.5f, 0.5f,
	          -0.5f, 0.5f, -0.5f,
	          -0.5f, 0.5f, 0.5f
	};
	
	private static float[] QUAD_RIGHT = {
	          // Right face
	          0.5f, -0.5f, 0.5f,	// 4
	          0.5f, -0.5f, -0.5f,	// 5
	          0.5f, 0.5f, 0.5f,		// 6
	          0.5f, 0.5f, -0.5f		// 7
	};
	
	private static float[] QUAD_FRONT = {
			  // Front face
			 -0.5f, -0.5f, 0.5f,	// 0 - Bottom Left
	          0.5f, -0.5f, 0.5f,	// 1 - Bottom Right
	         -0.5f,  0.5f, 0.5f,	// 2 - Top Left
	          0.5f,  0.5f, 0.5f	// 3 - Top Right
	};
	
	private static float[] QUAD_BACK = {
			  // Back face
			  0.5f, -0.5f, -0.5f,
			 -0.5f, -0.5f, -0.5f,
	          0.5f,  0.5f, -0.5f,
	         -0.5f,  0.5f, -0.5f
	};
}
