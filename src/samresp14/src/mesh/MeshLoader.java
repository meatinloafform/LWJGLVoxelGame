package samresp14.src.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import samresp14.src.Texture;

public class MeshLoader {
	private static List<Integer> vaos = new ArrayList<>();
	private static List<Integer> vbos = new ArrayList<>();
	
	public static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer createIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private static void storeData(int attribute, int dimensions, float[] data) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = createFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribute, dimensions, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private static void bindIndices(int[] data) {
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buffer = createIntBuffer(data);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	public static Mesh createMesh(float[] positions, float[] UVs, int[] indices, Texture texture) {
		int vao = genVAO();
		storeData(0, 3, positions);
		storeData(1, 2, UVs);
		bindIndices(indices);
		GL30.glBindVertexArray(0);
		return new Mesh(vao, indices.length, texture);
	}
	
	public static Mesh createQuad(Texture texture) {
		return createMesh(VERTICES_QUAD, UVS_QUAD, INDICES_QUAD, texture);
	}
	
	public static Mesh createCubeAll(Texture texture) {
		return createMesh(VERTICES_CUBE, UVS_CUBE_ALL, INDICES_CUBE, texture);
	}
	
	private static int genVAO() {
		int vao = GL30.glGenVertexArrays();
		vaos.add(vao);
		GL30.glBindVertexArray(vao);
		return vao;
	}
	
	public static final float[] VERTICES_QUAD = {
		 -0.5f, -0.5f, 0f,
          0.5f, -0.5f, 0f,
         -0.5f,  0.5f, 0f,
          0.5f,  0.5f, 0f
	};
	
	public static final int[] INDICES_QUAD = {
		0, 1, 2,
		1, 2, 3
	};
	
	public static final float[] UVS_QUAD = {
		0.0f, 0.0f,
		1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f,
	};
	
	/// One quad for each face
	public static final float[] VERTICES_CUBE = {
			
		  // Front face
		 -0.5f, -0.5f, 0.5f,	// 0 - Bottom Left
          0.5f, -0.5f, 0.5f,	// 1 - Bottom Right
         -0.5f,  0.5f, 0.5f,	// 2 - Top Left
          0.5f,  0.5f, 0.5f,	// 3 - Top Right
          
          // Right face
          0.5f, -0.5f, 0.5f,	// 4
          0.5f, -0.5f, -0.5f,	// 5
          0.5f, 0.5f, 0.5f,		// 6
          0.5f, 0.5f, -0.5f,	// 7
          
          // Left face
          -0.5f, -0.5f, 0.5f,
          -0.5f, -0.5f, -0.5f,
          -0.5f, 0.5f, 0.5f,
          -0.5f, 0.5f, -0.5f,
          
		  // Back face
		 -0.5f, -0.5f, -0.5f,
          0.5f, -0.5f, -0.5f,
         -0.5f,  0.5f, -0.5f,
          0.5f,  0.5f, -0.5f,
          
          // Top face
         -0.5f, 0.5f, 0.5f,
          0.5f, 0.5f, 0.5f,
         -0.5f, 0.5f, -0.5f,
          0.5f, 0.5f, -0.5f,
          
          // Bottom face
         -0.5f, -0.5f, 0.5f,
          0.5f, -0.5f, 0.5f,
         -0.5f, -0.5f, -0.5f,
          0.5f, -0.5f, -0.5f
	};
	
	public static final int[] INDICES_CUBE = {
		0, 1, 2,
		1, 2, 3,
		4, 5, 6,
		5, 6, 7,
		8, 9, 10,
		9, 10, 11,
		12, 13, 14,
		13, 14, 15,
		16, 17, 18,
		17, 18, 19,
		20, 21, 22,
		21, 22, 23
	};
	
	public static final float[] UVS_CUBE_ALL = {
		0.0f, 0.0f,
		1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f,	
		
		0.0f, 0.0f,
		1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f,
		
		0.0f, 0.0f,
		1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f,
		
		0.0f, 0.0f,
		1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f,
		
		0.0f, 0.0f,
		1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f,
		
		0.0f, 0.0f,
		1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f
	};
}
