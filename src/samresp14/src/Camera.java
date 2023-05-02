package samresp14.src;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
	private float[] angles;
	public Vector3f direction;
	public Vector3f position;
	public Vector3f up;
	public float speed;
	public float sensitivity;
	public boolean rotationFrozen;
	
	public Camera() {
		position = new Vector3f();
		angles = new float[2];
		up = new Vector3f(0f, 1f, 0f);
		direction = new Vector3f(0f, 0f, -1f);
		speed = 0.085f;
		sensitivity = 0.45f;
		rotationFrozen = false;
	}
	
	public void update() {
		if (!rotationFrozen) {
			Vector2f mouseDelta = Input.getMouseDelta();
			angles[0] -= mouseDelta.x * sensitivity;
			angles[1] -= mouseDelta.y * sensitivity;
		}
		
		if (angles[1] < -89f) {
			angles[1] = -89f;
		}
		if (angles[1] > 89f) {
			angles[1] = 89f;
		}
		
		direction = new Vector3f(
			(float)Math.cos(Math.toRadians(angles[1])) * (float)Math.sin(Math.toRadians(angles[0])),
			(float)Math.sin(Math.toRadians(angles[1])),
			(float)Math.cos(Math.toRadians(angles[1])) * (float)Math.cos(Math.toRadians(angles[0]))
		).normalize();
		
		if (Input.keyPressed(GLFW.GLFW_KEY_W)) {
			position.x += speed * (float)Math.sin(Math.toRadians(angles[0])) * (float)Math.cos(Math.toRadians(angles[1]));
			position.y += speed * (float)Math.sin(Math.toRadians(angles[1]));
			position.z += speed * (float)Math.cos(Math.toRadians(angles[0])) * (float)Math.cos(Math.toRadians(angles[1]));
		}
		if (Input.keyPressed(GLFW.GLFW_KEY_S)) {
			position.x -= speed * (float)Math.sin(Math.toRadians(angles[0])) * (float)Math.cos(Math.toRadians(angles[1]));
			position.y -= speed * (float)Math.sin(Math.toRadians(angles[1]));
			position.z -= speed * (float)Math.cos(Math.toRadians(angles[0])) * (float)Math.cos(Math.toRadians(angles[1]));
		}
		if (Input.keyPressed(GLFW.GLFW_KEY_A)) {
			float newAng = angles[0] + 270.0f;
			position.z -= speed * (float)Math.cos(Math.toRadians(newAng));
			position.x -= speed * (float)Math.sin(Math.toRadians(newAng));
		}
		if (Input.keyPressed(GLFW.GLFW_KEY_D)) {
			float newAng = angles[0] + 90.0f;
			position.z -= speed * (float)Math.cos(Math.toRadians(newAng));
			position.x -= speed * (float)Math.sin(Math.toRadians(newAng));
		}
	}
	
	public Matrix4f view() {
		Vector3f camTarget = new Vector3f(position.x + direction.x, position.y + direction.y, position.z + direction.z);
		return new Matrix4f().lookAt(position, camTarget, up).translate(-position.x, -position.y, -position.z);
	}
}
