package samresp14.src.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import samresp14.src.Camera;
import samresp14.src.Color;
import samresp14.src.shader.ShaderTextured;

public class RendererBundle {
	public DebugRenderer debug;
	public MeshRenderer mesh;
	public Camera camera;
	
	public void begin() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void setClearColor(Color color) {
		GL20.glClearColor(color.r, color.g, color.b, 1.0f);
	}
	
	public RendererBundle(Camera camera) {
		debug = new DebugRenderer(camera);
		mesh = new MeshRenderer(new ShaderTextured(), camera);
	}
	
	public void updateFov(float fov) {
		mesh.setFOV(fov);
		debug.setFOV(fov);
	}
	
	public void updateAspect(float aspect) {
		mesh.setAspect(aspect);
		debug.setAspect(aspect);
	}
}
