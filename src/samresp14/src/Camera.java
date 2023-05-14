package samresp14.src;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
	public float[] angles;
	public Vector3f direction;
	public Vector3f position;
	public Vector3f up;
	
	public Camera() {
		position = new Vector3f();
		angles = new float[2];
		up = new Vector3f(0f, 1f, 0f);
		direction = new Vector3f(0f, 0f, -1f);
	}
	
	public void update() {

	}
	
	public Matrix4f view() {
		Vector3f camTarget = new Vector3f(position.x + direction.x, position.y + direction.y, position.z + direction.z);
		return new Matrix4f().lookAt(position, camTarget, up);//.translate(-position.x, -position.y, -position.z);
	}
}
