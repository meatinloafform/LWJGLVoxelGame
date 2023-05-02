package samresp14.src;

import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.system.MemoryStack;

public final class Input {
	enum KeyState {
		Released, Pressed, JustPressed
	}
	
	static {
		keyStates = new KeyState[512];
		mouseStates = new KeyState[10];
		mousePosition = new Vector2f();
		lastMousePosition = new Vector2f();
	}
	
	public static void init(long window) {
		Input.window = window;
		for (int i = 0; i < 512; i++) {
			keyStates[i] = KeyState.Released;
			
			if (i < mouseStates.length) {
				mouseStates[i] = KeyState.Released;
			}
		}
	}
	
	public static void update() {
		for (int i = 0; i < keyStates.length; i++) {
			if (keyStates[i] == KeyState.JustPressed) {
				keyStates[i] = KeyState.Pressed;
			}
		}
		
		for (int i = 0; i < mouseStates.length; i++) {
			if (mouseStates[i] == KeyState.JustPressed) {
				mouseStates[i] = KeyState.Pressed;
			}
		}
		
		lastMousePosition = new Vector2f(mousePosition.x, mousePosition.y);
		mousePosition = getMousePosition();
		//System.out.println(lastMousePosition.x + "," + mousePosition.x);
	}
	
	public static GLFWKeyCallback keyboard = new GLFWKeyCallback() {
		
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (action == GLFW.GLFW_RELEASE) {
				keyStates[key] = KeyState.Released;
			} else {
				keyStates[key] = KeyState.JustPressed;
			}
		}
	};
	
	public static GLFWMouseButtonCallback mousePress = new GLFWMouseButtonCallback() {
		
		@Override
		public void invoke(long window, int button, int action, int mods) {
			if (action == GLFW.GLFW_RELEASE) {
				mouseStates[button] = KeyState.Released;
			} else {
				mouseStates[button] = KeyState.JustPressed;
			}
		}
	};
	
	private static long window;
	private static KeyState[] keyStates;
	private static KeyState[] mouseStates;
	public static Vector2f mousePosition;
	private static Vector2f lastMousePosition;
	
	public static void lockMouse() {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}
	
	public static void unlockMouse() {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}
	
	public static boolean keyJustPressed(int key) {
		return keyStates[key] == KeyState.JustPressed;
	}
	
	public static boolean keyPressed(int key) {
		return keyStates[key] == KeyState.Pressed || keyStates[key] == KeyState.JustPressed;
	}
	
	public static boolean keyReleased(int key) {
		return keyStates[key] == KeyState.Released || keyStates[key] == null;
	}
	
	public static boolean buttonJustPressed(int button) {
		return mouseStates[button] == KeyState.JustPressed;
	}
	
	public static boolean buttonPressed(int button) {
		return mouseStates[button] == KeyState.Pressed || keyStates[button] == KeyState.JustPressed;
	}
	
	public static boolean buttonReleased(int button) {
		return mouseStates[button] == KeyState.Released || keyStates[button] == null;
	}
	
	private static Vector2f getMousePosition() {
		DoubleBuffer pos = BufferUtils.createDoubleBuffer(2);
		try (MemoryStack stack = stackPush()) {
			DoubleBuffer pX = stack.mallocDouble(1);
			DoubleBuffer pY = stack.mallocDouble(1);
			
			GLFW.glfwGetCursorPos(window, pX, pY);
			
			return new Vector2f((float)pX.get(), (float)pY.get());
		}
	}
	
	public static Vector2f getMouseDelta() {
		// end - start
		return new Vector2f(mousePosition.x - lastMousePosition.x, mousePosition.y - lastMousePosition.y);
	}
}
