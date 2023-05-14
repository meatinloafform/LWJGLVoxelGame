package samresp14.src;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import samresp14.src.game.World;
import samresp14.src.mesh.Mesh;
import samresp14.src.shader.Shader;

public class Renderer {
	private Shader shader;
	private Matrix4f projection;
	private float fov;
	private Camera camera;
	private float aspect;
	
	public Renderer(Shader shader, Camera camera) {
		GL20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		this.shader = shader;
		this.camera = camera;
		projection = new Matrix4f().perspective(45.0f, 1.0f, 0.1f, 1000.0f);
		fov = 45.0f;
	}
	
	public void setAspect(float aspect) {
		this.aspect = aspect;
		projection = new Matrix4f().perspective(fov, aspect, 0.1f, 1000.0f);
	}
	
	public void setClearColor(Color color) {
		GL20.glClearColor(color.r, color.g, color.b, 1.0f);
	}
	
	public void setFOV(float fov) {
		this.fov = fov;
		projection = new Matrix4f().perspective(fov, aspect, 0.1f, 1000.0f);
	}
	
	public void begin() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void draw(Mesh mesh) {
		shader.use();
		shader.loadMatrix(shader.getUniformLocation("model"), mesh.getModel());
		shader.loadMatrix(shader.getUniformLocation("view"), camera.view());
		shader.loadMatrix(shader.getUniformLocation("projection"), projection);
		
		GL30.glBindVertexArray(mesh.getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		mesh.getTexture().bind();
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	public void free() {
		
	}
}
