package samresp14.src.rendering;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import samresp14.src.Camera;
import samresp14.src.Color;
import samresp14.src.mesh.MeshLoader;
import samresp14.src.shader.ShaderLines;
import samresp14.src.shader.ShaderProgram;

public class DebugRenderer {
	private ShaderProgram linesShader;
	private Matrix4f projection;
	private float fov;
	private Camera camera;
	private float aspect;
	
	private int lineVAO;
	private int linesCubeVAO;
	private static final float Z = 1f;
	
	public DebugRenderer(Camera camera) {
		this.camera = camera;
		linesShader = new ShaderLines();
		projection = new Matrix4f().perspective(45.0f, 1.0f, 0.1f, 1000.0f);
		fov = 45.0f;
		
		// create line vao
		lineVAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(lineVAO);
		int lineVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, lineVBO);
		FloatBuffer buffer = MeshLoader.createFloatBuffer(new float[] {
			0f, 0f, 0f, 1f, 1f, 1f
		});
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		linesCubeVAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(linesCubeVAO);
		int linesCubeVBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, linesCubeVBO);
		FloatBuffer cubeBuffer = MeshLoader.createFloatBuffer(new float[] {
				// Front face
				0f, 0f, 0f, 1f, 0f, 0f, // Bottom horiz
				0f, 1f, 0f, 1f, 1f, 0f, // Top horiz
				0f, 0f, 0f, 0f, 1f, 0f, // Left vert
				1f, 0f, 0f, 1f, 1f, 0f, // Right vert
				// Side faces
				0f, 0f, 0f, 0f, 0f, Z, // Bottom left
				1f, 0f, 0f, 1f, 0f, Z, // Bottom right
				0f, 1f, 0f, 0f, 1f, Z, // Top left
				1f, 1f, 0f, 1f, 1f, Z, // Top right
				// Back face
				0f, 0f, Z, 1f, 0f, Z, // Bottom horiz
				0f, 1f, Z, 1f, 1f, Z, // Top horiz
				0f, 0f, Z, 0f, 1f, Z, // Left vert
				1f, 0f, Z, 1f, 1f, Z, // Right vert
		});
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cubeBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	public void setAspect(float aspect) {
		this.aspect = aspect;
		projection = new Matrix4f().perspective(fov, aspect, 0.1f, 1000.0f);
	}
	

	public void setFOV(float fov) {
		this.fov = fov;
		projection = new Matrix4f().perspective(fov, aspect, 0.1f, 1000.0f);
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void drawLine(Vector3f start, Vector3f end, Color color) {
		linesShader.use();
		Matrix4f model = new Matrix4f().translate(start).scale(end.sub(start));
		linesShader.loadMatrix(linesShader.getUniformLocation("model"), model);
		linesShader.loadMatrix(linesShader.getUniformLocation("view"), camera.view());
		linesShader.loadMatrix(linesShader.getUniformLocation("projection"), projection);
		linesShader.loadVector3(linesShader.getUniformLocation("color"), color.asVector3f());
		
		GL30.glBindVertexArray(lineVAO);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public void drawCube(Vector3f start, Vector3f end, Color color) {
		linesShader.use();
		Matrix4f model = new Matrix4f().translate(start).scale(end.sub(start));
		linesShader.loadMatrix(linesShader.getUniformLocation("model"), model);
		linesShader.loadMatrix(linesShader.getUniformLocation("view"), camera.view());
		linesShader.loadMatrix(linesShader.getUniformLocation("projection"), projection);
		linesShader.loadVector3(linesShader.getUniformLocation("color"), color.asVector3f());
		
		GL30.glBindVertexArray(linesCubeVAO);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_LINES, 0, 24);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
