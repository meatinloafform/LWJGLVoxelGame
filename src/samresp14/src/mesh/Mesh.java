package samresp14.src.mesh;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;

import samresp14.src.Texture;

public class Mesh {
	private int vao;
	private int vertices;
	private Texture texture;
	private Matrix4f model;
	
	public Mesh(int vao, int vertices, Texture texture) {
		this.vao = vao;
		this.vertices = vertices;
		this.texture = texture;
		this.model = new Matrix4f();
	}
	
	public Matrix4f getModel() {
		return this.model;
	}
	
	public void setModel(Matrix4f model) {
		this.model = model;
	}
	
	public int getVAO() {
		return vao;	
	}
	
	public int getVertexCount() {
		return vertices;
	}
	
	public Texture getTexture() {
		return texture;
	}
}