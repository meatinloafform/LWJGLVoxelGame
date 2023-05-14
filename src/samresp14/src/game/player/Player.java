package samresp14.src.game.player;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;

import samresp14.src.Camera;
import samresp14.src.Color;
import samresp14.src.Input;
import samresp14.src.game.World;
import samresp14.src.game.blocks.BlockState;
import samresp14.src.game.collider.AABBCollider;
import samresp14.src.rendering.DebugRenderer;

public class Player {
	public Camera camera;
	public Vector3f position;
	public AABBCollider collider;
	public Vector3f velocity;
	public boolean rotationFrozen;
	public float speed;
	public float sensitivity;
	private boolean disconnectCamera;
	
	public static final int WORLD_COLLISION_PASSES = 3;
	
	public Player(Camera camera, Vector3f position) {
		this.camera = camera;
		this.position = position;
		this.collider = new AABBCollider(new Vector3f(), new Vector3f(1f, 2f, 1f));
		speed = 0.085f;
		sensitivity = 0.45f;
		rotationFrozen = false;
		updateColliderPosition();
	}
	
	private void updateColliderPosition() {
		collider.position = new Vector3f(
				camera.position.x - 0.5f,
				camera.position.y - 1.5f,
				camera.position.z - 0.5f
		);
	}
	
	public void disconnectCamera() {
		disconnectCamera = true;
	}
	
	public void connectCamera() {
		disconnectCamera = false;
	}
	
	public void move(Vector3f distance) {
		position.add(distance);
		collider.position.add(distance);
		camera.position.add(distance);
	}
	
	public void update(World world, DebugRenderer debug) {
		Vector3f camVel = new Vector3f();
		
		if (!rotationFrozen) {
			Vector2f mouseDelta = Input.getMouseDelta();
			camera.angles[0] -= mouseDelta.x * sensitivity;
			camera.angles[1] -= mouseDelta.y * sensitivity;
		}
		
		if (camera.angles[1] < -89f) {
			camera.angles[1] = -89f;
		}
		if (camera.angles[1] > 89f) {
			camera.angles[1] = 89f;
		}
		
		camera.direction = new Vector3f(
			(float)Math.cos(Math.toRadians(camera.angles[1])) * (float)Math.sin(Math.toRadians(camera.angles[0])),
			(float)Math.sin(Math.toRadians(camera.angles[1])),
			(float)Math.cos(Math.toRadians(camera.angles[1])) * (float)Math.cos(Math.toRadians(camera.angles[0]))
		).normalize();
		
		if (Input.keyPressed(GLFW.GLFW_KEY_W)) {
			camVel.x += speed * (float)Math.sin(Math.toRadians(camera.angles[0])) * (float)Math.cos(Math.toRadians(camera.angles[1]));
			camVel.y += speed * (float)Math.sin(Math.toRadians(camera.angles[1]));
			camVel.z += speed * (float)Math.cos(Math.toRadians(camera.angles[0])) * (float)Math.cos(Math.toRadians(camera.angles[1]));
		}
		if (Input.keyPressed(GLFW.GLFW_KEY_S)) {
			camVel.x -= speed * (float)Math.sin(Math.toRadians(camera.angles[0])) * (float)Math.cos(Math.toRadians(camera.angles[1]));
			camVel.y -= speed * (float)Math.sin(Math.toRadians(camera.angles[1]));
			camVel.z -= speed * (float)Math.cos(Math.toRadians(camera.angles[0])) * (float)Math.cos(Math.toRadians(camera.angles[1]));
		}
		if (Input.keyPressed(GLFW.GLFW_KEY_A)) {
			float newAng = camera.angles[0] + 270.0f;
			camVel.z -= speed * (float)Math.cos(Math.toRadians(newAng));
			camVel.x -= speed * (float)Math.sin(Math.toRadians(newAng));
		}
		if (Input.keyPressed(GLFW.GLFW_KEY_D)) {
			float newAng = camera.angles[0] + 90.0f;
			camVel.z -= speed * (float)Math.cos(Math.toRadians(newAng));
			camVel.x -= speed * (float)Math.sin(Math.toRadians(newAng));
		}
		
		//System.out.println(VectorUtil.vecToString(camVel));
		//this.velocity = camVel;
		move(camVel);
		
		for (int i = 0; i < WORLD_COLLISION_PASSES; i++) {
			Vector3f offset = collider.collidePlanarWorld(world);
//			if (offset.x + offset.y + offset.x == 0f) {
//				break;
//			}
			move(offset);
		}
	}
}
